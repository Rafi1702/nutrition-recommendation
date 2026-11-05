package com.example.nutritiontracker.data.mocks

import com.example.nutritiontracker.domain.model.QuantifiedFoodNutrients
import com.example.nutritiontracker.domain.model.RecommendationFood

val mockRecommendationFoods = MOCK_FOODS.map { food ->
    RecommendationFood(
        food = food,
        portion = 150.0f,
        estimatedCalories = 247.5f,
        similarityScore = 0.95f,
        nutrients = QuantifiedFoodNutrients(
            carbs = 0.0f,
            protein = 46.5f,
            fat = 5.4f,
            fibre = 0.0f,
            water = 105.0f
        )
    )
}