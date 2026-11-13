package com.example.nutritiontracker.domain.usecase.consumelog

import androidx.paging.PagingData
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConsumeLogUseCase @Inject constructor(private val consumeLogRepository: ConsumeLogRepository) {
    operator fun invoke(mealType: MealType, consumedAt: String): Flow<PagingData<ConsumeLog>> {
        return consumeLogRepository.getPersonalConsumeLog(mealType = mealType, consumedAt)
    }
}
