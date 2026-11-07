package com.example.nutritiontracker.data.model.recommendation_foods

import com.example.nutritiontracker.data.model.food.toFood
import com.example.nutritiontracker.domain.model.QuantifiedFoodNutrients
import com.example.nutritiontracker.domain.model.RecommendationFood

fun List<RecommendationFoodResponseDto>.toRecommendationFoods(): List<RecommendationFood> = this.map {
    it.toRecommendationFood()
}

fun RecommendationFoodResponseDto.toRecommendationFood(): RecommendationFood = RecommendationFood(
    food = this.food?.toFood(),
    portion = this.portion,
    estimatedCalories = this.estimatedCalories,
    similarityScore = this.similarityScore,
    nutrients = this.quantifiedFoodNutrientsDto?.toQuantifiedFoodNutrients(),
)

fun QuantifiedFoodNutrientsDto.toQuantifiedFoodNutrients(): QuantifiedFoodNutrients = QuantifiedFoodNutrients(
    carbs = this.quantisedCarbs,
    protein = this.quantisedProtein,
    fat = this.quantisedFat,
    fibre = this.quantisedFibre,
    water = this.quantisedWater,
)
