package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.model.NutrientsSubTotal
import com.example.nutritiontracker.domain.model.nutrientsSubTotal
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

private data class MealTypeProperties(
    val containerColor: Color,
    val contentColor: Color,
    val icon: ImageVector? = null,
)

@Composable
private fun MealType.properties(): MealTypeProperties = when (this) {
    MealType.BREAKFAST -> MealTypeProperties(
        containerColor = colorScheme.secondaryContainer,
        contentColor = colorScheme.onSecondaryContainer,
        icon = Icons.Default.Adb
    )

    else -> MealTypeProperties(
        containerColor = colorScheme.primaryContainer,
        contentColor = colorScheme.onPrimaryContainer,
        icon = Icons.Default.Adb
    )
}

@Preview(showBackground = true, backgroundColor = 4098)
@Composable
internal fun ConsumeLogSurface(consumeLog: List<ConsumeLog> = emptyList()) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MealType.entries.forEach { mealType ->
            ConsumeLogHeader(
                nutrientsSubTotal = consumeLog.nutrientsSubTotal(),
                mealType = mealType
            )
            Column(
                modifier = Modifier
                    .height(if (consumeLog.isEmpty()) 120.dp else 0.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (consumeLog.isEmpty()) {
                    Text(
                        "No Data",
                        style = typography.labelMedium.copy(color = colorScheme.onSurface)
                    )
                } else {
                    consumeLog.forEach { log -> FoodCard(log.food) }
                }
            }
        }
    }
}


@Composable
private fun ConsumeLogHeader(
    nutrientsSubTotal: NutrientsSubTotal? = null,
    mealType: MealType = MealType.BREAKFAST
) {

    val (containerColor, contentColor, icon) = mealType.properties()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(color = containerColor, shape = RoundedCornerShape(8.dp)) {
            icon?.let {
                Icon(
                    icon,
                    modifier = Modifier.padding(8.dp),
                    contentDescription = "${mealType.label}_consume_logs",
                    tint = contentColor
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(mealType.label, style = typography.titleMedium.copy(color = colorScheme.onSurface))
            nutrientsSubTotal?.let { subTotal ->
                Text(
                    "Subtotal: ${subTotal.calories} ${stringResource(R.string.total_calories_unit)} • ${
                        stringResource(
                            R.string.macronutrients_short_protein
                        )
                    }: ${subTotal.proteins} • ${
                        stringResource(
                            R.string.macronutrients_short_carbs
                        )
                    }: ${subTotal.carbs} •  ${
                        stringResource(
                            R.string.macronutrients_short_fat
                        )
                    }: ${subTotal.fats}",
                    style = typography.labelSmall.copy(color = colorScheme.onSurface)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        ShowMoreButton(minimumInteractive = true)
    }
}


private val DUMMY_NUTRIENTS_SUB_TOTAL = NutrientsSubTotal(
    calories = 100,
    proteins = 45.0
)

@Preview(showBackground = true)
@Composable
private fun ConsumeLogHeaderPreview() {
    ConsumeLogHeader(
        nutrientsSubTotal = DUMMY_NUTRIENTS_SUB_TOTAL, mealType = MealType.BREAKFAST
    )
}
