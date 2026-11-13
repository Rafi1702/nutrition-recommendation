package com.example.nutritiontracker.domain.usecase.consumelog

import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import javax.inject.Inject

class AddFoodsToConsumeLogUseCase @Inject constructor(
    private val consumeLogRepository: ConsumeLogRepository
) {
    suspend operator fun invoke(
        foodIds: List<Int>,
        mealType: MealType,
        consumedAt: String
    ): Result<Unit> {
        if (mealType == MealType.UNSPECIFIED) {
            return Result.failure(IllegalArgumentException("Meal type cannot be UNSPECIFIED"))
        }

        return consumeLogRepository.addFoodsToConsumeLog(
            meals = mapOf(mealType to foodIds),
            consumedAt = consumedAt
        )
    }

    suspend operator fun invoke(
        meals: Map<MealType, List<Int>>,
        consumedAt: String
    ): Result<Unit> {
        if(meals.isEmpty()) return Result.failure(ConsumeLogException.AddFoodToConsumeLogsException("Need at least one or more meal type selected"))
        return consumeLogRepository.addFoodsToConsumeLog(
            meals = meals,
            consumedAt = consumedAt
        )
    }
}
