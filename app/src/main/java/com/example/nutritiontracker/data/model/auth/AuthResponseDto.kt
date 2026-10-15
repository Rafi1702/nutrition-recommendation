package com.example.nutritiontracker.data.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_at")
    val expiresAt: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("refresh_expires_at")
    val refreshExpiresAt: String
)