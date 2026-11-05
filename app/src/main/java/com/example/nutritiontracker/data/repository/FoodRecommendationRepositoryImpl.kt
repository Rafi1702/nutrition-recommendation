package com.example.nutritiontracker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nutritiontracker.data.model.consumelog.toConsumeLog
import com.example.nutritiontracker.data.model.recommendation_foods.toRecommendationFood
import com.example.nutritiontracker.data.model.recommendation_foods.toRecommendationFoods
import com.example.nutritiontracker.data.remote.RecommendationFoodRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.PagingSource
import com.example.nutritiontracker.data.utils.defaultPageConfig
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.domain.repository.FoodRecommendationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FoodRecommendationRepositoryImpl @Inject constructor(
    private val recommendationFoodRemoteDataSource: RecommendationFoodRemoteDataSource,
) : FoodRecommendationRepository {

    override fun getRecommendedFoods(): Flow<PagingData<RecommendationFood>> {
        return Pager(
            config = defaultPageConfig(),
            pagingSourceFactory = {
                PagingSource(
                    initialCursor = 1,
                    networkCall = { cursor ->
                        safeCall {
                            recommendationFoodRemoteDataSource.getRecommendedFoods()
                        }
                    }
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { foodRecommendationResponse ->
                foodRecommendationResponse.toRecommendationFood()
            }
        }
    }
}
