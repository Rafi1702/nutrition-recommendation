package com.example.nutritiontracker.domain

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.nutritiontracker.ui.theme.colorScheme

enum class EatTime(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner")
}

data class ConsumeLog(
    val date: String,
    val breakfast: List<Food>,
    val lunch: List<Food>,
    val dinner: List<Food>
)
