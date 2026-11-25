package com.example.nutritiontracker.data.model.consumelog

import com.example.nutritiontracker.data.model.food.FoodResponseDto
import com.google.gson.annotations.SerializedName

data class GroupedConsumeLogResponseDto(
    @SerializedName("consumed_at")
    val consumedAt: String,

    val consumes: List<GroupedConsumeLogItem>
)

data class GroupedConsumeLogItem(
    val id: Int,

    @SerializedName("meal_type")
    val mealType: String,

    val food: FoodResponseDto
)


