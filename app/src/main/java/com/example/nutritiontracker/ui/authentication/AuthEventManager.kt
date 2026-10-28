package com.example.nutritiontracker.ui.authentication

import com.example.nutritiontracker.data.utils.NetworkException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventManager {
    private val _authEvent = MutableSharedFlow<NetworkException>()

    val authEvent = _authEvent.asSharedFlow()

    suspend fun triggerLogout(networkException: NetworkException) {
        _authEvent.emit(networkException)
    }
}