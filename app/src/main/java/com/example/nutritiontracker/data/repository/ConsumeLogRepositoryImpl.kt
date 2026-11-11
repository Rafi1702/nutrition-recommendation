package com.example.nutritiontracker.data.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.data.model.consumelog.ConsumeLogRequestDto
import com.example.nutritiontracker.data.model.consumelog.ConsumeMealRequestDto
import com.example.nutritiontracker.data.model.consumelog.toConsumeLog
import com.example.nutritiontracker.data.model.consumelog.toLabel
import com.example.nutritiontracker.data.remote.ConsumeLogRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.defaultPagerCreation
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConsumeLogRepositoryImpl @Inject constructor(
    private val consumeLogRemoteDataSource: ConsumeLogRemoteDataSource
) : ConsumeLogRepository {

    override fun getConsumeLog(currentTime: String): Flow<PagingData<ConsumeLog>> {
        throw NotImplementedError()
    }

    override suspend fun addFoodsToConsumeLog(
        meals: Map<MealType, List<Int>>,
        consumedAt: String
    ): Result<Unit> {
        return safeCall {
            consumeLogRemoteDataSource.addFoodsToConsumeLog(
                ConsumeLogRequestDto(
                    meals = meals.map { (mealType, foodIds) ->
                        ConsumeMealRequestDto(
                            foodIds = foodIds,
                            mealType = mealType.toLabel()
                        )
                    },
                    consumedAt = consumedAt
                )
            )
        }.fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { err -> Result.failure(err) }
        )
    }

    override suspend fun getPersonalConsumeLog(
        mealType: MealType,
        consumedAt: String
    ): Flow<PagingData<ConsumeLog>> {
        return defaultPagerCreation(
            ioCall = { cursor ->
                consumeLogRemoteDataSource.getConsumeLog(
                    buildMap {
                        putAll(
                            CursorPaginateQuery(
                                cursor = cursor,
                                limit = ITEMS_LIMIT
                            ).toMap()
                        )
                        put("meal_type", mealType.toString())
                        put("consumed_at", consumedAt)
                    }
                )
            },
            mapToData = { json ->
                json.toConsumeLog()
            }
        )
    }
}
