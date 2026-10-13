package com.example.nutritiontracker.domain.model

data class UserProfile(
    val gender: Gender,
    val dailyActivity: PersonalActivities,
    val birthDate: String? = null,
    val age: Int,
    val weight: Double,
    val height: Double,
) {
    val nutritionNeeds: Int
        get() = 0
}

fun createDefaultUserProfile(): UserProfile {
    return UserProfile(
        gender = Gender.MALE,
        dailyActivity = PersonalActivities.ACTIVE,
        birthDate = "",
        weight = .0,
        height = .0,
        age = 0
    )
}