package com.example.nutritiontracker.domain.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import kotlinx.coroutines.flow.Flow
import java.time.ZoneId
import java.time.ZonedDateTime

interface ConsumeLogRepository {
    suspend fun addFoodsToConsumeLog(
        meals: Map<MealType, List<Int>>,
        consumedAt: String
    ): Result<Unit>

    fun getPersonalConsumeLog(
        consumedAt: String? = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toString(),
        startDate: String? = null,
        endDate: String? = null,
        groupBy: String? = null,
    ): Flow<PagingData<ConsumeLog>>

    fun getGroupedPersonalConsumeLog()

    suspend fun getPersonalConsumeLogList(
        mealType: MealType,
        consumedAt: String = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toString()
    ): Result<List<ConsumeLog>>
}
