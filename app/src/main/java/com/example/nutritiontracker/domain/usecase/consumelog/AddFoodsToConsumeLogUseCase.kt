package com.example.nutritiontracker.domain.usecase.consumelog

import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.repository.ConsumeLogRepository
import javax.inject.Inject

class AddFoodsToConsumeLogUseCase @Inject constructor(private val consumeLogRepository: ConsumeLogRepository) {

    suspend operator fun invoke(
        foodIds: List<Int>,
        mealType: MealType,
        consumedAt: String
    ): Result<Unit> {
        return consumeLogRepository.addFoodsToConsumeLog(
            mealType = mealType,
            consumedAt = consumedAt,
            foodIds = foodIds
        ).fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { err ->
                Result.failure(err)
            }
        )
    }
}