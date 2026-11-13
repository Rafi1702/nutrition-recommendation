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

data class ChipBorder(
    val borderColor: Color = Color.Transparent,
    val borderWidth: Dp = 1.dp,
)

data class ChipColorStyle(
    val backgroundColor: Color,
    val contentColor: Color,
    val chipBorder: ChipBorder,
)

enum class ChipState {
    ENABLED, DISABLED
}

val FilledChipStyle: Map<ChipState, ChipColorStyle>
    @Composable get() = mapOf(
        ChipState.ENABLED to ChipColorStyle(
            backgroundColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer,
            chipBorder = ChipBorder()
        ),
        ChipState.DISABLED to ChipColorStyle(
            backgroundColor = colorScheme.primaryContainer.copy(alpha = 0.38f),
            contentColor = colorScheme.onPrimaryContainer.copy(alpha = 0.38f),
            chipBorder = ChipBorder()
        ),
    )

val OutlinedChipStyle: Map<ChipState, ChipColorStyle>
    @Composable get() = mapOf(
        ChipState.ENABLED to ChipColorStyle(
            backgroundColor = Color.Transparent,
            contentColor = colorScheme.primary,
            chipBorder = ChipBorder(borderColor = colorScheme.primary, borderWidth = 1.dp)
        ),
        ChipState.DISABLED to ChipColorStyle(
            backgroundColor = Color.Transparent,
            contentColor = colorScheme.onSurface.copy(alpha = 0.38f),
            chipBorder = ChipBorder(borderColor = colorScheme.onSurface.copy(alpha = 0.12f), borderWidth = 1.dp)
        ),
    )

fun Map<ChipState, ChipColorStyle>.defaultColor(chipState: ChipState = ChipState.ENABLED): ChipColorStyle {
    return this[chipState] ?: ChipColorStyle(
        backgroundColor = Color.Transparent,
        contentColor = Color.Unspecified,
        chipBorder = ChipBorder()
    )
}

fun Map<ChipState, ChipColorStyle>.defaultBorder(chipState: ChipState = ChipState.ENABLED): ChipBorder {
    return this[chipState]?.chipBorder ?: ChipBorder()
}

@Composable
internal fun Chip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    colors: ChipColorStyle = FilledChipStyle.defaultColor(),
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
        colors = ChipColorStyle(backgroundColor = color, contentColor = contentColor, chipBorder = ChipBorder()),
        content = content
    )
}

@Composable
internal fun Chip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    label: String,
    colors: ChipColorStyle = FilledChipStyle.defaultColor()
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
    selectedColors: ChipColorStyle = FilledChipStyle.defaultColor(),
    unselectedColors: ChipColorStyle = OutlinedChipStyle.defaultColor(),
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
        selectedColors = ChipColorStyle(backgroundColor = color, contentColor = contentColor, chipBorder = ChipBorder()),
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
