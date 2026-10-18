package com.example.nutritiontracker.ui.authentication

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventManager {
    private val _authEvent = MutableSharedFlow<Unit>()

    val authEvent = _authEvent.asSharedFlow()

    suspend fun triggerLogout() {
        _authEvent.emit(Unit)
    }
}