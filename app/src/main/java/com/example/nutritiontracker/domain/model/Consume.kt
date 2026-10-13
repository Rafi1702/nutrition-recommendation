package com.example.nutritiontracker.domain.model

enum class EatTime(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner")
}

data class NutrientsSubTotal(
    val calories: Int = 0,
    val carbs: Double = .0,
    val proteins: Double = .0,
    val fats: Double = .0,
)

data class ConsumeLog(
    val date: String,
    val breakfast: List<Food>,
    val lunch: List<Food>,
    val dinner: List<Food>
) {
    operator fun get(eatTime: EatTime): List<Food> {
        return when (eatTime) {
            EatTime.BREAKFAST -> breakfast
            EatTime.LUNCH -> lunch
            EatTime.DINNER -> dinner
        }
    }

    companion object{
        fun empty() : ConsumeLog{
           return ConsumeLog(
                date = "",
                breakfast = emptyList(),
                lunch = emptyList(),
                dinner = emptyList()
            )
        }
    }
}


fun ConsumeLog.nutrientsSubTotal(eatTime: EatTime): NutrientsSubTotal {
    return NutrientsSubTotal(
        calories = this[eatTime].fold(0) { acc, element -> acc + element.totalCalories },
        proteins = this[eatTime].fold(.0) { acc, element -> acc + element[MacroType.PROTEIN].serveValue },
        carbs = this[eatTime].fold(.0) { acc, element -> acc + element[MacroType.CARBS].serveValue },
        fats = this[eatTime].fold(.0) { acc, element -> acc + element[MacroType.FAT].serveValue },
    )
}

