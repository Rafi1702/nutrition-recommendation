package com.example.nutritiontracker.domain.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.RecommendationFood
import kotlinx.coroutines.flow.Flow

interface FoodRecommendationRepository {
    fun getRecommendedFoods(): Flow<PagingData<RecommendationFood>>
}
