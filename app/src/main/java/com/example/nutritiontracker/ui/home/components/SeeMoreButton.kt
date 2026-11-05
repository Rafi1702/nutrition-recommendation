package com.example.nutritiontracker.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@Composable
internal fun ShowMoreButton(minimumInteractive: Boolean = false, onClick: () -> Unit = {}) {
    val interactiveSize =
        if (minimumInteractive) 0.dp else LocalMinimumInteractiveComponentSize.current
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides interactiveSize) {
            IconButton(onClick = onClick){
                Icon(Icons.Default.Search, contentDescription = "see_more_icon_button")
            }
    }
}