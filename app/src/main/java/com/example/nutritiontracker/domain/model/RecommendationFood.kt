package com.example.nutritiontracker.domain.model

data class QuantifiedFoodNutrients(
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val fibre: Float?,
    val water: Float,
)

data class RecommendationFood(
    val food: Food,
    val portion: Float,
    val estimatedCalories: Float,
    val similarityScore: Float,
    val nutrients: QuantifiedFoodNutrients,
)
