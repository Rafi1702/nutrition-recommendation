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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.domain.ConsumeLog
import com.example.nutritiontracker.domain.EatTime
import com.example.nutritiontracker.domain.NutrientsSubTotal
import com.example.nutritiontracker.domain.nutrientsSubTotal
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

private data class EatTimeProperties(
    val containerColor: Color,
    val contentColor: Color,
    val icon: ImageVector? = null,
)

@Composable
private fun EatTime.properties(): EatTimeProperties = when (this) {
    EatTime.BREAKFAST -> EatTimeProperties(
        containerColor = colorScheme.secondaryContainer,
        contentColor = colorScheme.onSecondaryContainer,
        icon = Icons.Default.Adb
    )

    else -> EatTimeProperties(
        containerColor = colorScheme.primaryContainer,
        contentColor = colorScheme.onPrimaryContainer,
        icon = Icons.Default.Adb
    )
}

@Preview(showBackground = true, backgroundColor = 4098)
@Composable
internal fun ConsumeLogSurface(consumeLog: ConsumeLog = ConsumeLog.empty()) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EatTime.entries.forEach { eatTime ->
            ConsumeLogHeader(
                nutrientsSubTotal = consumeLog.nutrientsSubTotal(eatTime),
                eatTime = eatTime
            )


            Column(
                modifier = Modifier
                    .height(if (consumeLog[eatTime].isEmpty()) 120.dp else 0.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (consumeLog[eatTime].isEmpty()) {
                    Text(
                        "No Data",
                        style = typography.labelMedium.copy(color = colorScheme.onSurface)
                    )
                } else {
                    MOCK_FOODS.subList(0, 3).forEach { food -> FoodCard(food) }
                }

            }
        }
    }
}


@Composable
private fun ConsumeLogHeader(
    nutrientsSubTotal: NutrientsSubTotal? = null,
    eatTime: EatTime = EatTime.BREAKFAST
) {

    val (containerColor, contentColor, icon) = eatTime.properties()

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
                    contentDescription = "${eatTime.label}_consume_logs",
                    tint = contentColor
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(eatTime.label, style = typography.titleMedium.copy(color = colorScheme.onSurface))
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
        nutrientsSubTotal = DUMMY_NUTRIENTS_SUB_TOTAL, eatTime = EatTime.BREAKFAST
    )
}
