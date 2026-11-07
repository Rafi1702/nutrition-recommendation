package com.example.nutritiontracker.data.model.consumelog

import com.google.gson.annotations.SerializedName

data class ConsumeLogRequestDto(
    @SerializedName("food_ids")
    val foodIds: List<Int>,

    @SerializedName("meal_type")
    val mealType: String,

    @SerializedName("consumed_at")
    val consumedAt: String
)
