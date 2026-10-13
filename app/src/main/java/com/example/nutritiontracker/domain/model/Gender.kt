package com.example.nutritiontracker.domain.model

enum class Gender {
    MALE,
    FEMALE,
}

fun String.toGender(): Gender{
    return when(this){
        "Male" -> Gender.MALE
        "Female" -> Gender.FEMALE
        else -> Gender.MALE
    }
}