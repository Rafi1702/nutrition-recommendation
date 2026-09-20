package com.example.nutritiontracker.ui.theme

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

enum class ScreenSize{
    MOBILE,
    TABLET,
}



@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun rememberScreenSize(): ScreenSize {
    val activity = LocalActivity.current ?: return ScreenSize.MOBILE

    val windowSizeClass = calculateWindowSizeClass(activity)
    return if (
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    ) {
        ScreenSize.TABLET
    } else {
        ScreenSize.MOBILE
    }
}