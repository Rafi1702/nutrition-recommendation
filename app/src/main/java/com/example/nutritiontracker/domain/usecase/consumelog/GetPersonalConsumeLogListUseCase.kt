package com.example.nutritiontracker.domain.usecase.consumelog

import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import javax.inject.Inject

class GetPersonalConsumeLogListUseCase @Inject constructor(private val consumeLogRepository: ConsumeLogRepository) {
    suspend operator fun invoke(mealType: MealType, consumedAt: String): Result<List<ConsumeLog>> {
        return consumeLogRepository.getPersonalConsumeLogList(
            mealType = mealType,
            consumedAt = consumedAt
        )
    }
}