package com.example.nutritiontracker.data.model.user

import kotlinx.serialization.Serializable


@Serializable
data class UserProfileLocal(
    val gender: String,
    val personalActivities: String,
    val birth: String,
    val weight: Float,
    val height: Float,
    val userAllergies: List<String>,
)
