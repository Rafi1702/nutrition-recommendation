package com.example.nutritiontracker.domain.model

data class UserProfile(
    val gender: Gender,
    val dailyActivity: PersonalActivities,
    val birthDate: String? = null,
    val age: Int,
    val weight: Float,
    val height: Float,
    val needs: UserDailyNeeds,
    val allergies: List<Allergens> = emptyList(),
) {
    val nutritionNeeds: Int
        get() = 0
}

data class UserDailyNeeds(
    val breakfast: Float = 0f,
    val lunch: Float = 0f,
    val dinner: Float = 0f
)

fun createDefaultUserProfile(): UserProfile {
    return UserProfile(
        gender = Gender.MALE,
        dailyActivity = PersonalActivities.ACTIVE,
        birthDate = "",
        weight = 0f,
        height = 0f,
        age = 0,
        needs = UserDailyNeeds()
    )
}