package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.domain.EatTime
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
internal fun ConsumeLogSurface() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EatTime.entries.forEach { eatTime ->
            ConsumeLogHeader(eatTime)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MOCK_FOODS.subList(0, 3).forEach { food -> FoodCard(food) }
            }
        }
    }
}


@Composable
private fun ConsumeLogHeader(eatTime: EatTime = EatTime.BREAKFAST) {

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
        Text(eatTime.label, style = typography.titleMedium.copy(color = colorScheme.onSurface))
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = {}) {
            Text("See More")
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "${eatTime}_show_more"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ConsumeLogHeaderPreview() {
    ConsumeLogHeader(EatTime.BREAKFAST)
}
