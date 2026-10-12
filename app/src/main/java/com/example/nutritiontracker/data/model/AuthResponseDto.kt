package com.example.nutritiontracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val tokenType: String,
    val expiresAt: String,
)