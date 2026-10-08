package com.example.nutritiontracker.data.model

data class SignInRequestDto(
    val username: String,
    val password: String,
)

data class SignUpRequestDto(
    val username: String,
    val password: String,
    val confirmPassword: String,
)