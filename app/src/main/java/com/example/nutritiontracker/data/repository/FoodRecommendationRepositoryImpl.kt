package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.recommendation_foods.toRecommendationFoods
import com.example.nutritiontracker.data.remote.RecommendationFoodRemoteDataSource
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.domain.repository.FoodRecommendationRepository
import javax.inject.Inject

class FoodRecommendationRepositoryImpl @Inject constructor(
    private val recommendationFoodRemoteDataSource: RecommendationFoodRemoteDataSource,
) : FoodRecommendationRepository {

    override suspend fun getRecommendedFoods(): Result<List<RecommendationFood>> {
        val response = safeCall {
            recommendationFoodRemoteDataSource.getRecommendedFoods()
        }

        return response.fold(
            onSuccess = { res ->
                Result.success(res.data.toRecommendationFoods())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
