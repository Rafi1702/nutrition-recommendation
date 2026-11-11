package com.example.nutritiontracker.ui.authentication

import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.domain.AppException
import com.example.nutritiontracker.domain.AppException.Companion.createAppException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventManager {
    private val _authEvent = MutableSharedFlow<AppException>()

    val authEvent = _authEvent.asSharedFlow()

    suspend fun triggerLogout(exception: Exception) {
        when (exception) {
            is NetworkException -> {
                _authEvent.emit(exception.createAppException())
            }
            is AppException -> {
                _authEvent.emit(exception)
            }
        }
    }
}