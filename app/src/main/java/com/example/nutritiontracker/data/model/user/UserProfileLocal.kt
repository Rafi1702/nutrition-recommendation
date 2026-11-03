package com.example.nutritiontracker.data.model.user

import kotlinx.serialization.Serializable


@Serializable
data class UserProfileLocal(
    val gender: String,
    val personalActivities: String,
    val birth: String,
    val weight: Float,
    val height: Float,
    val userAllergiesIds: List<Int>,
)

fun createDefaultUserProfileLocal(): UserProfileLocal{
    return UserProfileLocal(
        gender = "Male",
        personalActivities = "Sedentary",
        birth = "",
        weight = 0f,
        height = 0f,
        userAllergiesIds = emptyList()
    )
}