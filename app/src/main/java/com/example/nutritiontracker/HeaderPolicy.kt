package com.example.nutritiontracker

enum class AuthorizationPolicy{
    BEARER,
    API_KEY;

    fun getAuthorizationRequestHeaderPair(value: String): Pair<String, String>{
        return when(this){
            BEARER -> ("Authorization" to "Bearer $value")
            API_KEY -> ("X-API-Key" to value)
        }
    }
}
enum class ContentType{
    JSON,
    HTML,
    XML;

    fun getContentTypeRequestHeaderPair(value: String): Pair<String, String>{
        return when(this){
            JSON -> TODO()
            HTML -> TODO()
            XML -> TODO()
        }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomHeader(val authPolicy: Array<AuthorizationPolicy>, val contentType: ContentType)

