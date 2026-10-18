package com.example.nutritiontracker.ui.utils.network


/*
TODO: This only possible using another client that support intercepting such as OkHttpClient
*/
enum class AuthorizationPolicy(val key: String){
    BEARER("Authorization"),
    API_KEY("X-API-KEY");

    fun getAuthorizationRequestHeaderPair(value: String): Pair<String, String>{
        return when(this){
            BEARER -> ("Authorization" to "Bearer $value")
            API_KEY -> ("X-API-Key" to value)
        }
    }
}
enum class ContentType(val value : String){
    JSON("application/json"),
    HTML(""),
    XML("");

}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomHeader(val authPolicy: Array<AuthorizationPolicy> = [], val contentType: ContentType)

