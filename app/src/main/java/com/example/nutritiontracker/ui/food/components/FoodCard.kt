package com.example.nutritiontracker.ui.food.components

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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.nutritiontracker.data.mocks.MOCK_FOODS
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MacroNutrients
import com.example.nutritiontracker.domain.model.MacroType
import com.example.nutritiontracker.domain.model.QuantifiedFoodNutrients
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.colors
import com.example.nutritiontracker.ui.theme.typography
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
private fun FoodCardPreview() {
    NutritionTrackerTheme {
        FoodCard(
            food = MOCK_FOODS[0]
        )
    }
}

@Composable
internal fun MacroChips(
    macros: List<MacroNutrients>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        macros.forEach { macro ->
            val (type, serve) = macro
            val macroColors = type.colors()
            Chip(
                radius = 12.dp,
                color = macroColors.container,
                contentColor = macroColors.onContainer
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

@Composable
internal fun FoodCard(
    modifier: Modifier = Modifier,
    name: String,
    totalCalories: Int,
    macros: List<MacroNutrients>,
    isLoading: Boolean = false,
    onAddClick: (() -> Unit)? = null
) {
    val totalCaloriesLabel = stringResource(R.string.total_calories_unit)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                                .background(colorScheme.primaryContainer.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = colorScheme.primary
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorScheme.primaryContainer.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = colorScheme.primary
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
                        text = "$totalCalories $totalCaloriesLabel",
                        style = typography.labelLarge.copy(
                            color = colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    MacroChips(macros = macros)
                }
            }
            onAddClick?.let {
                FilledTonalButton(
                    onClick = onAddClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    if (isLoading) CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colorScheme.onPrimary
                    ) else Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
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
}

@Composable
internal fun FoodCard(
    name: String,
    totalCalories: Int,
    carbs: Double,
    protein: Double,
    fat: Double,
    modifier: Modifier = Modifier,
    onAddClick: (() -> Unit)? = null
) {
    FoodCard(
        name = name,
        totalCalories = totalCalories,
        macros = listOf(
            MacroNutrients(MacroType.CARBS, carbs),
            MacroNutrients(MacroType.PROTEIN, protein),
            MacroNutrients(MacroType.FAT, fat)
        ),
        modifier = modifier,
        onAddClick = onAddClick
    )
}

@Composable
internal fun FoodCard(
    modifier: Modifier = Modifier,
    food: Food,
    isLoading: Boolean = false,
    onAddClick: (() -> Unit)? = null
) {
    FoodCard(
        name = food.name,
        totalCalories = food.totalCalories,
        macros = food.macros,
        modifier = modifier,
        isLoading = isLoading,
        onAddClick = onAddClick
    )
}
