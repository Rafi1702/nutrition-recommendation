package com.example.nutritiontracker.ui.theme

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class ScreenSize {
    MOBILE,
    FOLDABLE,
    TABLET,
}


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun rememberScreenSize(): ScreenSize {
//    val activity = LocalActivity.current ?: return ScreenSize.MOBILE
//
//    val windowSizeClass = calculateWindowSizeClass(activity)

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val customSizeClass = when {
        screenWidthDp < 600 -> ScreenSize.MOBILE
        screenWidthDp < 840 || screenWidthDp < 1200 -> ScreenSize.FOLDABLE
        screenWidthDp < 1600 -> ScreenSize.TABLET
        else -> ScreenSize.MOBILE
    }
//    return when (windowSizeClass.widthSizeClass){
//        WindowWidthSizeClass.Compact -> ScreenSize.MOBILE
//        WindowWidthSizeClass.Medium -> ScreenSize.FOLDABLE
//        WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Large -> ScreenSize.FOLDABLE
//        else -> ScreenSize.MOBILE
//    }
    return customSizeClass
}