package com.example.nutritiontracker.domain.usecase

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.domain.repository.FoodRecommendationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodRecommendationUseCase @Inject constructor(private val foodRecommendationRepository: FoodRecommendationRepository) {
    operator fun invoke(): Flow<PagingData<RecommendationFood>> {
        return foodRecommendationRepository.getRecommendedFoods()
    }
}