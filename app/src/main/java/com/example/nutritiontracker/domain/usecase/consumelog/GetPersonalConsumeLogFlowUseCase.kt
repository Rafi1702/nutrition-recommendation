package com.example.nutritiontracker.domain.usecase.consumelog

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import kotlinx.coroutines.flow.Flow

class GetPersonalConsumeLogFlowUseCase constructor(private val consumeLogRepository: ConsumeLogRepository) {
    operator fun invoke(
        mealType: MealType,
        consumedAt: String,
        groupBy: String? = null,
    ): Flow<PagingData<ConsumeLog>> {
        return consumeLogRepository.getPersonalConsumeLog(
            consumedAt = consumedAt,
            groupBy = groupBy
        )
    }
}