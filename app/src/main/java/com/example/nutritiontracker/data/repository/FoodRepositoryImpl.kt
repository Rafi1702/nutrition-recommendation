package com.example.nutritiontracker.data.repository

import androidx.paging.PagingConfig
import com.example.nutritiontracker.data.model.food.toFood
import com.example.nutritiontracker.data.model.food.toFoods
import com.example.nutritiontracker.data.remote.FoodRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.defaultPagerCreation
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.repository.FoodRepository
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodRemoteDataSource: FoodRemoteDataSource,
) : FoodRepository {


    override suspend fun getFoods(): Result<List<Food>> {
        val foodsResponse = safeCall {
            foodRemoteDataSource.getFoods(CursorPaginateQuery(cursor = 0, limit = 10).toMap())
        }
        return foodsResponse.fold(
            onSuccess = { res ->
                Result.success(res.data.items.toFoods())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    override fun getFoods(pagingConfig: PagingConfig) = defaultPagerCreation(ioCall = { cursor ->
        foodRemoteDataSource.getFoods(
            CursorPaginateQuery(cursor = cursor, limit = ITEMS_LIMIT).toMap()
        )
    }, mapToData = { json -> json.toFood() })
}
