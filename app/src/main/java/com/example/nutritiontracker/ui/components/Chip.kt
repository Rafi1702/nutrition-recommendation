package com.example.nutritiontracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val enabled: Boolean = false,
    val borderWidth: Dp,
    val disabledBorderColor: Color,
)


object FilledChipStyle {
    @Composable
    private fun defaultColor(): ChipColors {
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
    fun defaultBorder(): ChipBorder? {
        return null
    }
}


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
            Box(modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
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

@Composable
internal fun SelectionChip(
    modifier: Modifier = Modifier,
    radius: Dp? = null,
    selected: Boolean = false,
    color: Color = colorScheme.primaryContainer,
    contentColor: Color = colorScheme.onPrimaryContainer,
    borderColor: Color = colorScheme.primaryContainer,
    content: @Composable () -> Unit,
) {
    val backgroundColor = remember(selected) {
        if (selected) color else Color.Unspecified
    }

    val contentColor: Color = remember(selected) {
        if (selected) contentColor else Color.Unspecified
    }

    val borderColor: Color = remember(selected) {
        if (!selected) borderColor else Color.Unspecified
    }

    Surface(
        shape = if (radius != null) RoundedCornerShape(radius) else AssistChipDefaults.shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, borderColor),
        content = {
            Row(
                modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                if (selected) Icon(Icons.Default.Check, contentDescription = "selected_option")
                content()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectionChipPreview() {
    SelectionChip(selected = true) {
        Text("Preview")
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectionChipUnSelectedPreview() {
    SelectionChip(selected = false) {
        Text("Preview")
    }
}

