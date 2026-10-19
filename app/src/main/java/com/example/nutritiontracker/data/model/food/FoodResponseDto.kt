package com.example.nutritiontracker.data.model.food

import com.google.gson.annotations.SerializedName

class FoodResponseDto (
    val id: Int,
    val name: String,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    @SerializedName("total_calories")
    val totalCalories: Float,
    val water: Float,
    @SerializedName("group_id")
    val groupId: Int,
    @SerializedName("type_id")
    val typeId: Int
)