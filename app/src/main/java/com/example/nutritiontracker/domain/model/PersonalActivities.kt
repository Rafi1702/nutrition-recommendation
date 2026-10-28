package com.example.nutritiontracker.domain.model

enum class PersonalActivities {
    LIGHT,
    SEDENTARY,
    MODERATE,
    ACTIVE,
}

object PersonalActivitiesMap{
    fun String.toPersonalActivities(): PersonalActivities {
        return when (this) {
            "SEDENTARY" -> PersonalActivities.SEDENTARY
            "LIGHT" -> PersonalActivities.LIGHT
            "MODERATE" -> PersonalActivities.MODERATE
            "ACTIVE" -> PersonalActivities.ACTIVE
            else -> PersonalActivities.LIGHT
        }
    }
}