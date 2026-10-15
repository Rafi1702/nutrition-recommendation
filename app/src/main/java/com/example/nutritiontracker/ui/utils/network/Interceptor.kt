package com.example.nutritiontracker.ui.utils.network

import com.example.nutritiontracker.BuildConfig
import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.local.UserToken
import com.example.nutritiontracker.data.remote.AuthService
import kotlinx.coroutines.flow.first
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
                                runBlocking {
                                    localTokenDataSource.getUserToken().first().accessToken
                                }
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

    private fun updateUserToken(authService: AuthService): UserToken {
        return runBlocking {
            val oldToken = localTokenDataSource.getUserToken().first().refreshToken

            val updatedToken = oldToken?.let {
                val newToken = authService.refreshToken(oldToken)
                localTokenDataSource.setRefreshToken(newToken.refreshToken)
                newToken.accessToken
            }

            updatedToken?.let {
                localTokenDataSource.setToken(updatedToken)
            }

            localTokenDataSource.getUserToken().first()
        }
    }

    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        synchronized(this) {
            if (response.code == 401) {
                //CREATE new Client Builder to handle request to refresh token endpoint
                val refreshClient = OkHttpClient.Builder().build()
                val retrofitRefresh = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(refreshClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val authService = retrofitRefresh.create(AuthService::class.java)

                val newUserToken = updateUserToken(authService)

                newUserToken.accessToken?.let { accessToken ->
                    return chain.proceed(
                        request.newBuilder()
                            .header(AuthorizationPolicy.BEARER.key, "Bearer $accessToken")
                            .build()
                    )
                }
            }
            return response
        }
    }
}
