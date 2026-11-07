package com.example.nutritiontracker.domain.repository

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import kotlinx.coroutines.flow.Flow
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

interface ConsumeLogRepository {
    fun getConsumeLog(
        currentTime: String = ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toString()
    ): Flow<PagingData<ConsumeLog>>

    suspend fun addFoodsToConsumeLog(mealType: MealType, consumedAt: String, foodIds: List<Int>): Result<Unit>
}