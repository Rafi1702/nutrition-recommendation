package com.example.nutritiontracker.domain

import com.example.nutritiontracker.R


/*
    Label came from (string resource)
*/
enum class MacroType(label: Int) {
    PROTEIN(R.string.macronutrients_protein),
    CARBS(R.string.macronutrients_carbs), FAT(R.string.macronutrients_fat),
    CALORIES(0);
}

data class MacroNutrients(
    val type: MacroType,
    val serveValue: Double
)

data class Food(
    val id: String,
    val name: String,
    val macros: List<MacroNutrients>
)
