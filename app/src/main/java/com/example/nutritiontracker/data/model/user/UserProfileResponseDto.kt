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
    val height: Double,

    @SerializedName("weight")
    val weight: Double
)