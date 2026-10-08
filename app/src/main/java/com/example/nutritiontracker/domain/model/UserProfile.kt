package com.example.nutritiontracker.domain.model

data class UserProfile(
    val gender: Gender,
    val dailyActivity: PersonalActivities,
    val birthDate: String,
    val weight: Int,
    val height: Int,
) {
    val age: Int
        get() = 0

    val nutritionNeeds: Int
        get() = 0
}

fun createDefaultUserProfile(): UserProfile {
    return UserProfile(
        gender = Gender.MALE,
        dailyActivity = PersonalActivities.ACTIVE,
        birthDate = "",
        weight = 0,
        height = 0
    )
}