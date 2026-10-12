package com.example.nutritiontracker.ui.utils.network

import okhttp3.Response
import retrofit2.Invocation
import okhttp3.Interceptor as OkHttpInterceptor

class AuthorizationInterceptor : OkHttpInterceptor {
    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val newRequest = request.newBuilder()

        invocation?.let {
            val method = invocation.method() ?: return chain.proceed(request)

            val annotation =
                method.getAnnotation(CustomHeader::class.java) ?: return chain.proceed(request)

            annotation.authPolicy.forEach { authorizationPolicy ->
               val headerValue = when(authorizationPolicy){
                    AuthorizationPolicy.BEARER -> {
                        //TODO: Call the Bearer
                        "Bearer "

                    }

                    AuthorizationPolicy.API_KEY -> {
                        //TODO: Get the x-api-key from local.properties
                        "X-API-LATER-HAHAHAH"
                    }
                }

                newRequest.addHeader(authorizationPolicy.name,headerValue)
            }
        }
        return chain.proceed(newRequest.build())
    }
}

class ContentTypeInterceptor: OkHttpInterceptor{
    override fun intercept(chain: OkHttpInterceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val newRequest = request.newBuilder()

        invocation?.let {
            val method = invocation.method() ?: return chain.proceed(request)

            val annotation =
                method.getAnnotation(CustomHeader::class.java) ?: return chain.proceed(request)

            newRequest.addHeader("Content-Type", annotation.contentType.name)
        }
        return chain.proceed(newRequest.build())
    }
}