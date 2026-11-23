package com.example.nutritiontracker.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import kotlinx.coroutines.launch


data class DropdownSurfaceStyle(
    val backgroundColor: Color,
    val contentColor: Color,
    val iconTint: Color,
    val chipBorder: BorderStroke,
    val shape: CornerBasedShape,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentPadding: PaddingValues,
    val verticalAlignment: Alignment.Vertical,
)

data class DropdownItemProps(
    val itemLabel: String,
    val autoClose: Boolean = true,
    val onClick: (() -> Unit)? = null
)


val DefaultDropdownSurfaceStyle: DropdownSurfaceStyle
    @Composable get() = DropdownSurfaceStyle(
        backgroundColor = colorScheme.surfaceVariant,
        contentColor = colorScheme.onSurfaceVariant,
        chipBorder = BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f)),
        iconTint = colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        contentHorizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(Spacing.m, Spacing.s)
    )


@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    items: List<DropdownItemProps>,
    selectedLabel: String,
    surfaceStyle: DropdownSurfaceStyle = DefaultDropdownSurfaceStyle,
    prefixIcon: (@Composable () -> Unit)? = null,
    suffixIcon: (@Composable () -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var surfaceWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = Modifier.onSizeChanged { size ->
            surfaceWidth = with(density) { size.width.toDp() }
        }
    ) {
        Surface(
            onClick = {
                scope.launch {
                    rotation.animateTo(
                        targetValue = 180f,
                        animationSpec = tween(100, easing = LinearEasing)
                    )
                    expanded = true
                }
            },
            shape = surfaceStyle.shape,
            color = surfaceStyle.backgroundColor,
            border = surfaceStyle.chipBorder
        ) {
            Row(
                modifier = Modifier.padding(surfaceStyle.contentPadding),
                horizontalArrangement = surfaceStyle.contentHorizontalArrangement,
                verticalAlignment = surfaceStyle.verticalAlignment
            ) {
                prefixIcon?.invoke()
                Text(
                    text = selectedLabel,
                    modifier = Modifier.width(48.dp),
                    style = typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                (suffixIcon ?: {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "selection_mode_dropdown_icon",
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(rotation.value)
                    )
                }).invoke()
            }
        }

        DropdownMenu(
            modifier = Modifier.width(surfaceWidth),
            expanded = expanded,
            onDismissRequest = {
                scope.launch {
                    rotation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(100, easing = LinearEasing)
                    )
                }
                expanded = false
            }
        ) {
            items.forEach { props ->
                DropdownMenuItem(
                    text = { Text(props.itemLabel) },
                    onClick = {
                        props.onClick?.let {
                            it()
                            expanded = !props.autoClose
                        }
                    }
                )
            }
        }
    }
}