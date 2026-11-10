package com.example.nutritiontracker.domain

import com.example.nutritiontracker.data.utils.NetworkException
import java.net.ConnectException

sealed class AppException(override val message: String) : Exception(message) {

    sealed class Network(message: String) : AppException(message) {
        data object Unauthorized : Network("Unauthorized access")
        data object ConnectionFailed : Network("No network connection")
    }


    open class DomainException(override val message: String): AppException(message)

    companion object {
        fun NetworkException.createAppException(): AppException {
            return when (this) {
                is NetworkException.Unauthorized -> Network.Unauthorized
                else -> Network.ConnectionFailed
            }
        }
    }
}