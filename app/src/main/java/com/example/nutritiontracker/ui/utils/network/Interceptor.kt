package com.example.nutritiontracker.ui.utils.network

import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.local.UserToken
import com.example.nutritiontracker.data.model.auth.RefreshTokenRequestDto
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.ui.authentication.AuthEventManager
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation
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
                                runBlocking { localTokenDataSource.getUserToken().accessToken }
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

class MockUnauthorizedInterceptor : OkHttpInterceptor {
    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()

        if (request.url.encodedPath.contains("/user/profile")) {
            val errorBody = "{\"message\": \"Unauthorized\"}"
            val response = Response.Builder()
                .code(401)
                .protocol(Protocol.HTTP_1_1)
                .message("Unauthorized")
                .body(errorBody.toResponseBody("application/json".toMediaType()))
                .request(request)
                .addHeader("Content-Type", "application/json")
                .build()

            if (response.code == 401) {
                runBlocking { AuthEventManager.triggerLogout(NetworkException.Unauthorized()) }
            }

            return response
        }
        return Response.Builder().code(200).build()
    }
}

class ResponseInterceptor(
    private val localTokenDataSource: LocalTokenDataSource,
    private val authService: AuthService
) : OkHttpInterceptor {

    private fun updateUserToken(): Result<UserToken> {
        val oldToken = runBlocking { localTokenDataSource.getUserToken() }

        val refreshToken = oldToken.refreshToken ?: return Result.failure(Exception("No refresh token available"))

        val refreshTokenResponse = runBlocking {
            authService.refreshToken(RefreshTokenRequestDto(refreshToken = refreshToken))
        }

        if (refreshTokenResponse.code() != 200 || refreshTokenResponse.body() == null) {
            return Result.failure(
                NetworkException.create(
                    refreshTokenResponse.code(),
                    errorBody = refreshTokenResponse.errorBody()?.string()
                )
            )
        }

        val body = refreshTokenResponse.body()!!.data
        runBlocking {
            localTokenDataSource.setToken(body.accessToken)
            localTokenDataSource.setRefreshToken(body.refreshToken)
        }

        val newStoreToken = runBlocking { localTokenDataSource.getUserToken() }
        return Result.success(newStoreToken)
    }

    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code != 401) return response

        synchronized(this) {
            val currentToken = runBlocking { localTokenDataSource.getUserToken() }
            val requestToken = request.header(AuthorizationPolicy.BEARER.key)

            // Double-check: If another concurrent request already refreshed the token while we were waiting,
            // retry this request immediately with the newly stored token.
            if (requestToken != null && requestToken != "Bearer ${currentToken.accessToken}") {
                response.close()
                val newRequest = request.newBuilder()
                    .header(AuthorizationPolicy.BEARER.key, "Bearer ${currentToken.accessToken}")
                    .header("Content-Type", ContentType.JSON.value)
                    .build()
                return chain.proceed(newRequest)
            }

            val newUserToken = updateUserToken()

            return newUserToken.fold(
                onSuccess = { (accessToken, _) ->
                    response.close()
                    val newRequest = request.newBuilder()
                        .header(AuthorizationPolicy.BEARER.key, "Bearer $accessToken")
                        .header("Content-Type", ContentType.JSON.value)
                        .build()

                    chain.proceed(newRequest)
                },
                onFailure = { err ->
                    response.close()
                    if (err is NetworkException) {
                        runBlocking {
                            localTokenDataSource.clearToken()
                            AuthEventManager.triggerLogout(err)
                        }
                    }
                    response
                }
            )
        }
    }
}
