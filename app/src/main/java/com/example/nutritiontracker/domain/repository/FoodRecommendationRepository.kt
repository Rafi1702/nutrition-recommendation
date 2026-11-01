package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.Food

interface FoodRecommendationRepository {
    suspend fun getRecommendedFoods(): Result<List<Food>>
}
