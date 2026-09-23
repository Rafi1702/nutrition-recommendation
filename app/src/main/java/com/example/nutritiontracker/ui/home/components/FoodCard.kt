package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.domain.MacroNutrients
import com.example.nutritiontracker.domain.MacroType
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
private fun FoodCardPreview() {
    NutritionTrackerTheme {
        FoodCard(
            food = Food(
                id = "0",
                name = "Nasi Goreng Ayam",
                macros = listOf(
                    MacroNutrients(type = MacroType.PROTEIN, serveValue = 18.0),
                    MacroNutrients(type = MacroType.CARBS, serveValue = 45.0),
                    MacroNutrients(type = MacroType.FAT, serveValue = 12.0)
                )
            )
        )
    }
}

@Composable
internal fun FoodCard(
    food: Food,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {}
) {
    val (_, name, macros) = food
    val totalCaloriesLabel = stringResource(R.string.total_calories)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SubcomposeAsyncImage(
                    model = name,
                    contentDescription = name,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = name,
                        style = typography.titleMedium.copy(
                            color = colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        text = "${food.totalCalories} $totalCaloriesLabel",
                        style = typography.labelLarge.copy(
                            color = colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        macros.forEach { macro ->
                            val (type, serve) = macro
                            val (chipBg, chipFg) = when (type) {
                                MacroType.PROTEIN -> colorScheme.tertiaryContainer to colorScheme.onTertiaryContainer
                                MacroType.CARBS -> colorScheme.primaryContainer to colorScheme.onPrimaryContainer
                                MacroType.FAT -> colorScheme.secondaryContainer to colorScheme.onSecondaryContainer
                            }
                            Chip(
                                radius = 12.dp,
                                color = chipBg,
                                contentColor = chipFg
                            ) {
                                Text(
                                    text = "${serve.roundToInt()}g ${stringResource(type.label)}",
                                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 6.dp),
                                    style = typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }

            FilledTonalButton(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "add"
                    )
                    Text(text = stringResource(R.string.add_to_consume_log))
                }
            }
        }
    }
}
