package com.example.nutritiontracker.data.model.user


data class UserProfileCreateRequestDto(
    val gender: String,
    val personalActivities: String,
    val birth: String,
    val weight: Float,
    val height: Float,
    val userAllergies: List<String>
)
