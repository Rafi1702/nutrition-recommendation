package com.example.nutritiontracker.domain.model

import kotlin.math.roundToInt

enum class MealType(val label: String) {
    UNSPECIFIED(""),
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner")
}

data class NutrientsSubTotal(
    val calories: Int = 0,
    val carbs: Int = 0,
    val proteins: Int = 0,
    val fats: Int = 0,
)

data class ConsumeLog(
    val mealType: MealType,
    val consumeLogDetail: ConsumeLogDetail,
)

data class ConsumeLogDetail(
    val id: Int,
    val food: Food,
    val eatTime: String,
)

fun List<ConsumeLog>.nutrientsSubTotal(): NutrientsSubTotal {
//    val totalCalories = sumOf { it.food.totalCalories }
//    val totalCarbs = sumOf { it.food[MacroType.CARBS].serveValue }
//    val totalProteins = sumOf { it.food[MacroType.PROTEIN].serveValue }
//    val totalFats = sumOf { it.food[MacroType.FAT].serveValue }
    return NutrientsSubTotal(
        calories = 0,
        carbs = 0,
        proteins = 0,
        fats = 0,
    )
}
