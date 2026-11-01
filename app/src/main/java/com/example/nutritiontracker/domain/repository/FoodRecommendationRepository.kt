package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.RecommendationFood

interface FoodRecommendationRepository {
    suspend fun getRecommendedFoods(): Result<List<RecommendationFood>>
}
