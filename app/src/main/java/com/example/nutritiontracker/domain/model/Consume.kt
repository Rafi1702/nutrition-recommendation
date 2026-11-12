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
    val id: Int,
    val food: Food,
    val eatTime: String,
    val mealType: MealType
)

fun List<ConsumeLog>.nutrientsSubTotal(): NutrientsSubTotal {
    val totalCalories = sumOf { it.food.totalCalories }
    val totalCarbs = sumOf { it.food[MacroType.CARBS].serveValue }
    val totalProteins = sumOf { it.food[MacroType.PROTEIN].serveValue }
    val totalFats = sumOf { it.food[MacroType.FAT].serveValue }
    return NutrientsSubTotal(
        calories = totalCalories,
        carbs = totalCarbs.roundToInt(),
        proteins = totalProteins.roundToInt(),
        fats = totalFats.roundToInt()
    )
}
