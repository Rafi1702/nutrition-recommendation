package com.example.nutritiontracker.domain.model

enum class PersonalActivities(val label: String) {
    SEDENTARY("sedentary"),
    LIGHT("light"),
    MODERATE("moderate"),
    ACTIVE("active"),
}

object PersonalActivitiesMap{
    fun String.toPersonalActivities(): PersonalActivities {
        return when (this) {
            "sedentary" -> PersonalActivities.SEDENTARY
            "light" -> PersonalActivities.LIGHT
            "moderate" -> PersonalActivities.MODERATE
            "active" -> PersonalActivities.ACTIVE
            else -> PersonalActivities.LIGHT
        }
    }
}