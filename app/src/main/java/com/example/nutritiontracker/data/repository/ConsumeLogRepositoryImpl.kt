package com.example.nutritiontracker.data.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.data.model.consumelog.ConsumeLogRequestDto
import com.example.nutritiontracker.data.model.consumelog.ConsumeMealRequestDto
import com.example.nutritiontracker.data.model.consumelog.toConsumeLog
import com.example.nutritiontracker.data.remote.ConsumeLogRemoteDataSource
import com.example.nutritiontracker.data.utils.CursorPaginateQuery
import com.example.nutritiontracker.data.utils.ITEMS_LIMIT
import com.example.nutritiontracker.data.utils.PageCursor
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
    override suspend fun addFoodsToConsumeLog(
        meals: Map<MealType, List<Int>>,
        consumedAt: String
    ): Result<Unit> {
        val request = ConsumeLogRequestDto(
            meals = meals.map { (mealType, foodIds) ->
                ConsumeMealRequestDto(
                    foodIds = foodIds,
                    mealType = mealType.ordinal
                )
            },
        )

        return safeCall {
            consumeLogRemoteDataSource.addFoodsToConsumeLog(
                request
            )
        }.fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { err -> Result.failure(err) }
        )
    }

    override fun getPersonalConsumeLog(
        consumedAt: String,
        startDate: String?,
        endDate: String?,
        groupBy: String?,
    ): Flow<PagingData<ConsumeLog>> {
        return defaultPagerCreation(
            initialCursor = PageCursor.Cursor(value = null),
            networkCallDelay = 500,
            ioCall = { cursor ->
                consumeLogRemoteDataSource.getPersonalConsumeLogs(
                    buildMap {
                        putAll(
                            CursorPaginateQuery(
                                cursor = cursor,
                                limit = ITEMS_LIMIT
                            ).toMap()
                        )
                        put("consumed_at", consumedAt)
                        put("meal_type", 1)
                        groupBy?.let {
                            put("group_by", groupBy)
                        }
                        startDate?.let {
                            put("start_date", startDate)
                        }
                        endDate?.let {
                            put("end_date", endDate)
                        }
                    }
                )
            },
            mapToData = { json ->
                json.toConsumeLog()
            }
        )
    }

    override suspend fun getPersonalConsumeLogList(
        mealType: MealType,
        consumedAt: String
    ): Result<List<ConsumeLog>> {
        return safeCall {
            consumeLogRemoteDataSource.getPersonalConsumeLogs(
                buildMap {
                    put("meal_type", mealType.ordinal)
                    put("consumed_at", consumedAt)
                }
            )
        }.fold(
            onSuccess = { data ->
                Result.success(data.data.items.map { consumeLog -> consumeLog.toConsumeLog() })
            },
            onFailure = { err ->
                Result.failure(err)
            }
        )
    }
}
