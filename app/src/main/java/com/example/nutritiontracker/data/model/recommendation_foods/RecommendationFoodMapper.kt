package com.example.nutritiontracker.data.model.recommendation_foods

import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MacroNutrients
import com.example.nutritiontracker.domain.model.MacroType

fun List<RecommendationFoodResponseDto>.toFoods(): List<Food> = this.map {
    it.toFood()
}

fun RecommendationFoodResponseDto.toFood(): Food = Food(
    id = this.id,
    name = this.name,
    macros = this.createMacros(),
)

private fun RecommendationFoodResponseDto.createMacros(): List<MacroNutrients> {
    return listOf(
        MacroNutrients(
            type = MacroType.CARBS,
            serveValue = this.carbs.toDouble()
        ),
        MacroNutrients(
            type = MacroType.FAT,
            serveValue = this.fat.toDouble()
        ),
        MacroNutrients(
            type = MacroType.PROTEIN,
            serveValue = this.protein.toDouble()
        )
    )
}
