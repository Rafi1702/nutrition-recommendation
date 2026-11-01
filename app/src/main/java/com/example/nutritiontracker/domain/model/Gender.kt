package com.example.nutritiontracker.domain.model

enum class Gender(val label: String) {
    MALE("male"),
    FEMALE("female"),
}

object GenderMap {
    fun String.toGender(): Gender {
        return when (this) {
            "male" -> Gender.MALE
            "female" -> Gender.FEMALE
            else -> Gender.MALE
        }
    }
}
