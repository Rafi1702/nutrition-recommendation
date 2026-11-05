package com.example.nutritiontracker.data.model.recommendation_foods

import com.example.nutritiontracker.data.model.food.FoodResponseDto
import com.google.gson.annotations.SerializedName

data class RecommendationFoodResponseDto(
    val food: FoodResponseDto,

    @SerializedName("portion_g")
    val portion: Float,

    @SerializedName("estimated_calories")
    val estimatedCalories: Float,

    @SerializedName("similiarity_score")
    val similarityScore: Float,

    @SerializedName("nutrients")
    val quantifiedFoodNutrientsDto: QuantifiedFoodNutrientsDto

)

data class QuantifiedFoodNutrientsDto(
    @SerializedName("carbs_g")
    val quantisedCarbs: Float,

    @SerializedName("protein_g")
    val quantisedProtein: Float,

    @SerializedName("fat_g")
    val quantisedFat: Float,

    @SerializedName("fibre_g")
    val quantisedFibre: Float?,

    @SerializedName("water_g")
    val quantisedWater: Float,
)


