package com.example.nutritiontracker.data.model.auth

import com.google.gson.annotations.SerializedName

data class SignInRequestDto(
    val username: String,
    val password: String,
)

data class SignUpRequestDto(
    val username: String,
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String,
)

data class RefreshTokenRequestDto(
    @SerializedName("refresh_token")
    val refreshToken: String,
)