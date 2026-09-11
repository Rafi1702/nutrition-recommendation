package com.example.nutritiontracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalTypography = compositionLocalOf { AppTypography }


val typography: Typography
    @Composable
    get() = LocalTypography.current


val LocalColorScheme = compositionLocalOf { LightColorScheme }

val colorScheme: ColorScheme
    @Composable
    get() = LocalColorScheme.current
