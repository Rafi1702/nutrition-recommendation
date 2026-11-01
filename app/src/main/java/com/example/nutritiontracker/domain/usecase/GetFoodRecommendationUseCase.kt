package com.example.nutritiontracker.domain.usecase

import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.domain.repository.FoodRecommendationRepository
import javax.inject.Inject

class GetFoodRecommendationUseCase @Inject constructor(private val foodRecommendationRepository: FoodRecommendationRepository) {
    suspend operator fun invoke(): Result<List<RecommendationFood>> {
        return foodRecommendationRepository.getRecommendedFoods()
    }
}