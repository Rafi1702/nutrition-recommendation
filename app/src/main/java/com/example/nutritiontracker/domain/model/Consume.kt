package com.example.nutritiontracker.domain.model

enum class MealType(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner")
}


data class NutrientsSubTotal(
    val calories: Int = 0,
    val carbs: Double = 0.0,
    val proteins: Double = 0.0,
    val fats: Double = 0.0,
)

data class ConsumeLog(
    val id: Int,
    val userId: Int,
    val food: Food,
    val eatTime: String,
    val mealType: MealType
)

fun List<ConsumeLog>.nutrientsSubTotal(): NutrientsSubTotal {
    return this.fold(
        NutrientsSubTotal()
    ) { acc, consume ->
        NutrientsSubTotal(
            calories = acc.calories + consume.food.totalCalories,
            carbs = acc.carbs + consume.food[MacroType.CARBS].serveValue,
            proteins = acc.proteins + consume.food[MacroType.PROTEIN].serveValue,
            fats = acc.fats + consume.food[MacroType.FAT].serveValue,
        )
    }
}