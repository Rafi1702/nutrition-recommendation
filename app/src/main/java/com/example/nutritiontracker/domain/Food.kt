package com.example.nutritiontracker.domain

import com.example.nutritiontracker.R
import kotlin.math.roundToInt


/*
    Label came from (string resource)
*/
enum class MacroType(val label: Int) {
    PROTEIN(R.string.macronutrients_protein),
    CARBS(R.string.macronutrients_carbs),
    FAT(R.string.macronutrients_fat),
}

data class MacroNutrients(
    val type: MacroType,
    val serveValue: Double
)

/*
    The macros should be based on enum order
    to correctly calculate totalCalories
*/
data class Food(
    val id: String,
    val name: String,
    val macros: List<MacroNutrients>
){
    val totalCalories: Int
        get() = (macros[0].serveValue * 4 + macros[1].serveValue * 4 + macros[2].serveValue * 9).roundToInt()
}
