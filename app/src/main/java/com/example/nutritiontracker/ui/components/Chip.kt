package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

data class ChipColors(
    val backgroundColor: Color,
    val contentColor: Color,
)

data class ChipBorder(
    val borderColor: Color = Color.Transparent,
    val borderWidth: Dp = 1.dp,
)

object FilledChipStyle {
    @Composable
    fun defaultColor(): ChipColors {
        return ChipColors(
            backgroundColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer
        )
    }
}

object OutlinedChipStyle {
    @Composable
    fun defaultColor(): ChipColors {
        return ChipColors(
            backgroundColor = Color.Transparent,
            contentColor = colorScheme.primary
        )
    }

    @Composable
    fun defaultBorder(): ChipBorder {
        return ChipBorder(
            borderColor = colorScheme.primary,
            borderWidth = 1.dp
        )
    }
}

@Composable
internal fun Chip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    colors: ChipColors = FilledChipStyle.defaultColor(),
    content: @Composable () -> Unit,
) {
    Surface(
        shape = if (radius != null) RoundedCornerShape(radius) else AssistChipDefaults.shape,
        color = colors.backgroundColor,
        contentColor = colors.contentColor,
        content = {
            Box(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                content()
            }
        }
    )
}

@Composable
internal fun Chip(
    color: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    content: @Composable () -> Unit,
) {
    Chip(
        modifier = modifier,
        radius = radius,
        colors = ChipColors(backgroundColor = color, contentColor = contentColor),
        content = content
    )
}

@Composable
internal fun Chip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    label: String,
    colors: ChipColors = FilledChipStyle.defaultColor()
) {
    Chip(modifier = modifier, radius = radius, colors = colors) {
        Text(
            text = label,
            modifier = modifier,
            style = typography.labelSmall
        )
    }
}

@Composable
internal fun SelectionChip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    selected: Boolean = false,
    selectedColors: ChipColors = FilledChipStyle.defaultColor(),
    unselectedColors: ChipColors = OutlinedChipStyle.defaultColor(),
    border: ChipBorder? = if (!selected) OutlinedChipStyle.defaultBorder() else null,
    content: @Composable () -> Unit,
) {
    val colors = if (selected) selectedColors else unselectedColors
    val borderStroke = if (selected) {
        BorderStroke(1.dp, Color.Transparent)
    } else {
        border?.let { BorderStroke(it.borderWidth, it.borderColor) }
    }

    Surface(
        shape = if (radius != null) RoundedCornerShape(radius) else AssistChipDefaults.shape,
        color = colors.backgroundColor,
        contentColor = colors.contentColor,
        border = borderStroke,
        content = {
            Row(
                modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "selected_option",
                        modifier = Modifier.size(16.dp)
                    )
                }
                content()
            }
        }
    )
}

@Composable
internal fun SelectionChip(
    selected: Boolean,
    color: Color,
    contentColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    content: @Composable () -> Unit,
) {
    SelectionChip(
        modifier = modifier,
        radius = radius,
        selected = selected,
        selectedColors = ChipColors(backgroundColor = color, contentColor = contentColor),
        unselectedColors = OutlinedChipStyle.defaultColor(),
        border = if (!selected) ChipBorder(borderColor = borderColor) else null,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectionChipPreview() {
    SelectionChip(selected = true) {
        Text("Selected")
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectionChipUnSelectedPreview() {
    SelectionChip(selected = false) {
        Text("Unselected")
    }
}
