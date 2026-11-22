package com.example.nutritiontracker.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

fun Modifier.defaultCardElevation(): Modifier {
    return this.shadow(4.dp, RectangleShape)
}