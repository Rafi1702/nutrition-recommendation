package com.example.nutritiontracker.data.mocks

import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType

val MOCK_CONSUME_LOGS = MOCK_FOODS.mapIndexed { index, food ->
    ConsumeLog(
        id = index,
        food = food,
        eatTime = "",
        mealType = MealType.BREAKFAST
    )
}