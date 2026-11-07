package com.example.nutritiontracker.domain

import com.example.nutritiontracker.data.utils.NetworkException
import java.net.ConnectException

sealed class AppException(override val message: String) : Exception(message) {
    object ConnectionFailed : AppException("No Connection Found")
    object NoNetwork : AppException("No Network Provided")

    data object NoSession : AppException("Unauthorized Access")

    companion object {
        fun NetworkException.createAppException(): AppException {
            return when (this) {
                is NetworkException.Unauthorized -> NoSession
                else -> NoNetwork
            }
        }
    }
}