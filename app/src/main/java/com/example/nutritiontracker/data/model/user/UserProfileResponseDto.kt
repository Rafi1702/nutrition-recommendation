package com.example.nutritiontracker.data.model.user

import com.google.gson.annotations.SerializedName


data class UserProfileResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("height")
    val height: Float,

    @SerializedName("weight")
    val weight: Float,

    @SerializedName("bmr")
    val bmr: Float?,

    @SerializedName("needs")
    val needs: UserDailyNeedsDto
)

data class UserDailyNeedsDto(
    @SerializedName("breakfast")
    val breakfast: Float = 0f,

    @SerializedName("lunch")
    val lunch: Float = 0f,

    @SerializedName("dinner")
    val dinner: Float = 0f
)