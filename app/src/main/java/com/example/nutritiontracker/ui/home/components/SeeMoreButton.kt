package com.example.nutritiontracker.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.ScreenSize
import com.example.nutritiontracker.ui.theme.screenSize

@Composable
internal fun ShowMoreButton(minimumInteractive: Boolean = false, onClick: () -> Unit = {}) {
    val interactiveSize =
        if (minimumInteractive) 0.dp else LocalMinimumInteractiveComponentSize.current
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides interactiveSize) {
        if(screenSize != ScreenSize.FOLDABLE){
            TextButton(onClick = onClick) {
                Text("See More")
            }
        }else{
            IconButton(onClick = onClick){
                Icon(Icons.Default.Search, contentDescription = "see_more_icon_button")
            }
        }
    }
}