package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.domain.MacroType
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.colors
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle


@Preview(showBackground = true)
@Composable
private fun MacroNutrientsNeedPreview() {
    NutritionTrackerTheme {
        FlowRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MacroNutrientsNeed(
                label = MacroType.PROTEIN,
                current = 45f,
                target = 120f,
                unit = "g"
            )
            MacroNutrientsNeed(
                label = MacroType.CARBS,
                current = 150f,
                target = 250f,
                unit = "g"
            )
            MacroNutrientsNeed(
                label = MacroType.FAT,
                current = 30f,
                target = 65f,
                unit = "g"
            )
        }
    }
}

@Composable
internal fun MacroNutrientsNeed(
    label: MacroType,
    current: Float,
    target: Float,
    unit: String,
    modifier: Modifier = Modifier
) {
    val macroColors = label.colors()

    Card(
        modifier = modifier
            .width(130.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .background(macroColors.main)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(label.label),
                        style = typography.labelMedium.copy(
                            color = colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.Fastfood,
                        contentDescription = "${label.name}-icon",
                        modifier = Modifier.size(16.dp),
                        tint = macroColors.main
                    )
                }
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = typography.headlineSmall,
                        color = colorScheme.onSurface
                    ) {
                        append(current.toInt().toString())
                    }

                    withStyle(
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    ) {
                        append("/${target.toInt()}$unit")
                    }
                })
                val remaining = (target - current).toInt().coerceAtLeast(0)
                Text(
                    text = "Sisa $remaining$unit",
                    style = typography.bodySmall.copy(color = colorScheme.onSurfaceVariant)
                )
                LinearProgressIndicator(
                    progress = { (current / target).coerceIn(0f, 1f) },
                    color = macroColors.main,
                    trackColor = macroColors.container,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }
        }
    }
}
