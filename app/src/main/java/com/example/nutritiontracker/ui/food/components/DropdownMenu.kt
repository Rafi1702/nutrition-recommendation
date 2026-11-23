package com.example.nutritiontracker.ui.food.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import androidx.compose.material3.DropdownMenuItem as M3DropdownMenuItem

@Composable
internal fun DropdownMenuItem(
    selected: Boolean,
    label: String,
    onClick: () -> Unit = {},
) {
    M3DropdownMenuItem(
        modifier = Modifier
            .padding(horizontal = Spacing.s)
            .selectedDropdownItemShape(selected),
        text = { Text(label) },
        onClick = onClick
    )
}


@Composable
internal fun Modifier.selectedDropdownItemShape(selected: Boolean) = composed {
    if (selected) this.background(
        colorScheme.onSurfaceVariant,
        RoundedCornerShape(8.dp)
    ) else this
}
