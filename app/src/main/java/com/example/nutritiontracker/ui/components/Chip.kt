package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


@Composable
internal fun Chip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    color: Color = colorScheme.primaryContainer,
    contentColor: Color = colorScheme.onPrimaryContainer,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = if (radius != null) RoundedCornerShape(radius) else AssistChipDefaults.shape,
        color = color,
        contentColor = contentColor,
        content = {
            Box(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)){
                content()
            }
        }
    )
}

@Composable
internal fun Chip(modifier: Modifier = Modifier, radius: Dp? = null, label: String) {
    Chip(modifier, radius) {
        Text(
            label,
            modifier = modifier,
            style = typography.labelSmall
        )
    }
}
