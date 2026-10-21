package com.example.nutritiontracker.data.model.consumelog

import com.example.nutritiontracker.data.model.food.FoodResponseDto
import com.google.gson.annotations.SerializedName

data class ConsumeLogResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("food")
    val food: FoodResponseDto,

    @SerializedName("eat_time")
    val eatTime: String,

    @SerializedName("meal_type")
    val mealType: String
)