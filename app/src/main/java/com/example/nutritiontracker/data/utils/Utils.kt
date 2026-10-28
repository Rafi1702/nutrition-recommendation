package com.example.nutritiontracker.data.utils

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import retrofit2.Response
import java.io.IOException


const val ITEMS_LIMIT = 10


data class Response<out T>(
    val message: String,
    val data: T,
)

data class PaginatedListResponse<out T>(
    val message: String,
    val data: CursorPaginate<T>
)

data class CursorPaginate<out T>(
    @SerializedName("next_cursor")
    val nextCursor: Int,
    @SerializedName("has_more")
    val hasMore: Boolean,
    val items: List<T>
)

data class CursorPaginateQuery(
    val cursor: Int,
    val limit: Int = ITEMS_LIMIT,
) {
    fun toMap(): Map<String, Int> {
        return mapOf(
            "cursor" to cursor,
            "limit" to limit
        )
    }
}

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


fun <T> withRetry(apiCall: () -> Response<T>): Flow<T> {
    return flow {
        val data = safeCall(apiCall).getOrThrow()
        emit(data)
    }.retryWhen { cause, attempt ->
        (cause is IOException || cause is NetworkException) && attempt < 3
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