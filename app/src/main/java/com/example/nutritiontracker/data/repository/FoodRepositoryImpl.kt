package com.example.nutritiontracker.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.nutritiontracker.data.model.food.toFood
import com.example.nutritiontracker.data.model.food.toFoods
import com.example.nutritiontracker.data.remote.FoodRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.data.utils.PagingSource
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getFoods(pagingConfig: PagingConfig): Flow<PagingData<Food>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                PagingSource(
                    initialCursor = 1,
                    networkCall = { cursor ->
                        safeCall {
                            foodRemoteDataSource.getFoods(
                                CursorPaginateQuery(cursor = cursor, limit = ITEMS_LIMIT).toMap()
                            )
                        }
                    }
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { foodResponseDto ->
                foodResponseDto.toFood()
            }
        }
    }
}
