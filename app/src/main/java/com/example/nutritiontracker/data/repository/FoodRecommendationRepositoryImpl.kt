package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.recommendation_foods.toRecommendationFood
import com.example.nutritiontracker.data.remote.RecommendationFoodRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.defaultPagerCreation
import com.example.nutritiontracker.domain.repository.FoodRecommendationRepository
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class FoodRecommendationRepositoryImpl @Inject constructor(
    private val recommendationFoodRemoteDataSource: RecommendationFoodRemoteDataSource,
) : FoodRecommendationRepository {

    override fun getRecommendedFoods() = defaultPagerCreation(ioCall = { cursor ->
        recommendationFoodRemoteDataSource.getRecommendedFoods(
            CursorPaginateQuery(
                cursor = cursor,
                limit = ITEMS_LIMIT
            ).toMap()
        )
    }, mapToData = { json -> json.toRecommendationFood() })
}
