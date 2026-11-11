package com.example.nutritiontracker.data.model.consumelog

import com.google.gson.annotations.SerializedName

data class ConsumeMealRequestDto(
    @SerializedName("food_ids")
    val foodIds: List<Int>,

    @SerializedName("meal_type")
    val mealType: Int,
)

data class ConsumeLogRequestDto(
    @SerializedName("meals")
    val meals: List<ConsumeMealRequestDto>,

    @SerializedName("consumed_at")
    val consumedAt: String? = null
)
