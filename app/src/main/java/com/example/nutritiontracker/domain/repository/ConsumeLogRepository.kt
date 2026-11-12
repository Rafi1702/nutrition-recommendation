package com.example.nutritiontracker.domain.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import kotlinx.coroutines.flow.Flow
import java.time.ZoneId
import java.time.ZonedDateTime

interface ConsumeLogRepository {
    fun getConsumeLog(
        currentTime: String = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toString()
    ): Flow<PagingData<ConsumeLog>>

    suspend fun addFoodsToConsumeLog(
        meals: Map<MealType, List<Int>>,
        consumedAt: String
    ): Result<Unit>

    fun getPersonalConsumeLog(
        mealType: MealType,
        consumedAt: String = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toString()
    ): Flow<PagingData<ConsumeLog>>

    suspend fun getPersonalConsumeLogList(
        mealType: MealType,
        consumedAt: String = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toString()
    ): Result<List<ConsumeLog>>
}
