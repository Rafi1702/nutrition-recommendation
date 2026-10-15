package com.example.nutritiontracker.ui.utils.network

import com.example.nutritiontracker.BuildConfig
import com.example.nutritiontracker.data.local.LocalTokenDataSource
import com.example.nutritiontracker.data.remote.AuthService
import com.example.nutritiontracker.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor as OkHttpInterceptor


class HeaderInterceptor : OkHttpInterceptor {
    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val newRequest = request.newBuilder()

        invocation?.let {
            val method = invocation.method() ?: return chain.proceed(request)

            val annotation =
                method.getAnnotation(CustomHeader::class.java) ?: return chain.proceed(request)

            annotation.authPolicy.forEach { authorizationPolicy ->
                val headerValue = when (authorizationPolicy) {
                    AuthorizationPolicy.BEARER -> {
                        //TODO: Call the Bearer from local datastore
                        val token = "TOKEN_EXAMPLE"
                        "Bearer $token"
                    }

                    AuthorizationPolicy.API_KEY -> {
                        //TODO: Get the x-api-key from local.properties
                        "X-API-KEY"
                    }
                }

                newRequest.addHeader(authorizationPolicy.key, headerValue)
            }

            newRequest.addHeader("Content-Type", annotation.contentType.name)
        }
        return chain.proceed(newRequest.build())
    }
}

class ResponseInterceptor(private val localTokenDataSource: LocalTokenDataSource) :
    OkHttpInterceptor {
    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        synchronized(this) {
            if (response.code == 401) {
                //TODO: hit the refresh token api
                val refreshClient = OkHttpClient.Builder().build()
                val retrofitRefresh = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(refreshClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val authService = retrofitRefresh.create(AuthService::class.java)
                val storeToken = runBlocking {
                   val updatedToken =  authService.refreshToken("")
                    localTokenDataSource.setToken(updatedToken)
                }

                runBlocking {

                }

            }
        }

        TODO("Not yet implemented")
    }

    fun

}
