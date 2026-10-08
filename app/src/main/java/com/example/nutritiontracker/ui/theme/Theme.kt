package com.example.nutritiontracker.ui.theme

    import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.SnackbarHostState
    import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun NutritionTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = LocalColorScheme.current

    CompositionLocalProvider(
        LocalTypography provides typography,
        LocalScreenSize provides screenSize,
        LocalColorScheme provides colorScheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}