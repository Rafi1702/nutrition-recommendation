package com.example.nutritiontracker.ui.home.components

import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@Composable
internal fun ShowMoreButton(minimumInteractive: Boolean = false, onClick: () -> Unit = {}) {
    val interactiveSize =
        if (minimumInteractive) 0.dp else LocalMinimumInteractiveComponentSize.current
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides interactiveSize) {
        TextButton(onClick = onClick) {
            Text("See More")
        }
    }
}