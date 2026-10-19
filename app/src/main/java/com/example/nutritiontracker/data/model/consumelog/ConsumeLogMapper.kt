package com.example.nutritiontracker.data.model.consumelog

import com.example.nutritiontracker.data.model.food.toFood
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType

fun String.toMealType(): MealType {
    return when (this.lowercase()) {
        "lunch" -> MealType.LUNCH
        "dinner" -> MealType.DINNER
        else -> MealType.BREAKFAST
    }
}

fun ConsumeLogResponseDto.toConsumeLog(): ConsumeLog = ConsumeLog(
    id = this.id,
    userId = this.userId,
    food = this.food.toFood(),
    eatTime = this.eatTime,
    mealType = this.mealType.toMealType(),
)

fun List<ConsumeLogResponseDto>.toConsumeLogList(): List<ConsumeLog> =
    this.map { response -> response.toConsumeLog() }