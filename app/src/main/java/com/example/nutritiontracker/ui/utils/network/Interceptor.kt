package com.example.nutritiontracker.ui.utils.network

import android.util.Log
import com.example.nutritiontracker.BuildConfig
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.local.UserToken
import com.example.nutritiontracker.data.model.auth.RefreshTokenRequestDto
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.ui.authentication.AuthEventManager
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor as OkHttpInterceptor


class HeaderInterceptor(private val localTokenDataSource: LocalTokenDataSource) :
    OkHttpInterceptor {
    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val newRequest = request.newBuilder()

        invocation?.let {
            val method = invocation.method() ?: return chain.proceed(request)

            val annotation =
                method.getAnnotation(CustomHeader::class.java) ?: return chain.proceed(request)

            synchronized(this) {
                annotation.authPolicy.forEach { authorizationPolicy ->
                    val headerValue = when (authorizationPolicy) {
                        AuthorizationPolicy.BEARER -> {
                            val accessToken =
                                localTokenDataSource.getUserToken().accessToken
                            accessToken?.let {
                                "Bearer $accessToken"
                            }
                        }

                        AuthorizationPolicy.API_KEY -> {
                            //TODO: Get the x-api-key from local.properties
                            val apiKey: String? = null

                            apiKey
                        }
                    }

                    headerValue?.let {
                        newRequest.addHeader(authorizationPolicy.key, headerValue)
                    }
                }

                newRequest.addHeader("Content-Type", annotation.contentType.name)
            }
        }

        return chain.proceed(newRequest.build())
    }
}

class ResponseInterceptor(private val localTokenDataSource: LocalTokenDataSource) :
    OkHttpInterceptor {

    private fun updateUserToken(authService: AuthService): Result<UserToken> {
        val oldToken = localTokenDataSource.getUserToken()

        val refreshTokenResponse = oldToken.refreshToken?.let {
            runBlocking {
                authService.refreshToken(RefreshTokenRequestDto(refreshToken = oldToken.refreshToken))
            }
        }

        refreshTokenResponse?.let { response ->
            if (response.code() != 200) return Result.failure(
                NetworkException.GenericHttpError(
                    refreshTokenResponse.code(),
                    message = response.message()
                )
            )

            response.body()?.let { body ->
                localTokenDataSource.setToken(body.accessToken)
                localTokenDataSource.setRefreshToken(body.refreshToken)
            }
            val newStoreToken = localTokenDataSource.getUserToken()

            Log.d(
                "[TOKEN_COMPARISON]",
                "refresh_token: ${oldToken.refreshToken == newStoreToken.refreshToken}, access_token: ${oldToken.accessToken == newStoreToken.accessToken}"
            )

            return Result.success(newStoreToken)
        }

        return Result.failure(Exception("Unknown Error"))
    }

    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        synchronized(this) {
            if (response.code == 401) {
                val refreshClient = OkHttpClient.Builder().build()
                val retrofitRefresh = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(refreshClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val authService = retrofitRefresh.create(AuthService::class.java)

                val newUserToken = updateUserToken(authService)

                var finalResponse: Response? = null
                newUserToken.fold(
                    onSuccess = { (accessToken, refreshToken) ->
                        response.close()
                        val newRequest = request.newBuilder()
                            .header(AuthorizationPolicy.BEARER.key, "Bearer $accessToken")
                            .header("Content-Type", ContentType.JSON.value)
                            .build()

                        finalResponse = chain.proceed(newRequest)

                    },
                    onFailure = { err ->
                        if (err is NetworkException.GenericHttpError) {
                            runBlocking { AuthEventManager.triggerLogout() }
                        }
                        finalResponse = response
                    }
                )
                return finalResponse!!
            }
        }
        return response
    }
}
