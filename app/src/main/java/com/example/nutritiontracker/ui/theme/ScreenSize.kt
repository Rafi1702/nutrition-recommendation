package com.example.nutritiontracker.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class ScreenSize(val maxWidthConstraint: Int) {
    MOBILE(600),
    FOLDABLE(1200),
    TABLET(1600);
}


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun rememberScreenSize(): ScreenSize {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val customSizeClass = when {
        screenWidthDp < ScreenSize.MOBILE.maxWidthConstraint -> ScreenSize.MOBILE
        screenWidthDp < ScreenSize.FOLDABLE.maxWidthConstraint -> ScreenSize.FOLDABLE
        screenWidthDp < ScreenSize.TABLET.maxWidthConstraint -> ScreenSize.TABLET
        else -> ScreenSize.MOBILE
    }

    Log.d("[SCREEN_SIZE]", "$customSizeClass")
    return customSizeClass
}