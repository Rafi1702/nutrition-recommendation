package com.example.nutritiontracker.data

import retrofit2.Response
import java.io.IOException

sealed class NetworkException(val httpCode: Int, override val message: String) :
    Exception(message) {
    class NotFound(code: Int = 404) : NetworkException(code, "Resource Not Found")
    class Unauthorized(code: Int = 401) : NetworkException(code, "Unauthorized Access")
    class ServerError(code: Int = 500) : NetworkException(code, "Internal Server Error")
    class GenericHttpError(code: Int, message: String) : NetworkException(code, message)

    companion object {
        fun create(code: Int, errorBody: String?): NetworkException {
            return when (code) {
                401 -> Unauthorized(code)
                404 -> NotFound(code)
                500 -> ServerError(code)
                else -> GenericHttpError(
                    code,
                    errorBody?.takeIf { it.isNotBlank() } ?: "HTTP Error: $code")
            }
        }
    }
}

inline fun <T> safeCall(apiCall: () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(NullPointerException("Response body is null"))
            }
        } else {
            val code = response.code()
            val errorBody = response.errorBody()?.string()
            Result.failure(NetworkException.create(code, errorBody))
        }
    } catch (e: IOException) {

        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}