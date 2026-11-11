package com.example.nutritiontracker.data.model.consumelog

import com.example.nutritiontracker.data.model.food.FoodResponseDto
import com.google.gson.annotations.SerializedName

data class ConsumeLogResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("food")
    val food: FoodResponseDto,

    @SerializedName("consumed_at")
    val consumedAt: String,
)

data class ConsumeMealLogResponseDto(
    @SerializedName("meal_type")
    val mealType: String,

    @SerializedName("consumes")
    val consumes: List<ConsumeLogResponseDto>
)
