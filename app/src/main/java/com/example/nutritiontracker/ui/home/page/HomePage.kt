package com.example.nutritiontracker.ui.home.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.ui.home.components.FoodCard
import com.example.nutritiontracker.ui.home.components.MacroNutrientsNeed
import com.example.nutritiontracker.ui.home.components.UserNeedsCard
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.Debounce
import kotlinx.coroutines.delay


@Composable
fun HomePage(modifier: Modifier = Modifier) {
    var recommendationFoods by remember { mutableStateOf(MOCK_FOODS) }
    Debounce("User Needs") {
        getFoods()
    }
    LazyColumn(modifier = modifier) {
        item {
            UserNeedsCard()

        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            UserNeedSection()
        }

        recommendedFoodSection(recommendationFoods)
    }
}

private fun LazyListScope.recommendedFoodSection(recommendationFoods: List<Food> = emptyList()) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Rekomendasi menu hari ini")
        Spacer(modifier = Modifier.height(8.dp))
    }
    itemsIndexed(recommendationFoods, key = { _, item -> item.id }) { index, food ->
        if (index > 0) {
            Spacer(modifier = Modifier.height(8.dp))
        }
        FoodCard(food)
    }
}

private suspend fun getFoods(): List<Food> {
    delay(300)
    return MOCK_FOODS
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF808080
)
@Composable
private fun UserCardPreview() {
    UserNeedsCard()
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFF808080
)
@Composable
private fun UserNeedSection(modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Makro Nutrisi Hari Ini", style = typography.labelLarge)
            Text(
                "Target gram",
                style = typography.labelSmall.copy(color = LocalContentColor.current.copy(alpha = .5f))
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MacroNutrientsNeed(
                label = "PROTEIN",
                current = 90f,
                target = 130f,
                unit = "g"
            )
            MacroNutrientsNeed(
                label = "PROTEIN",
                current = 90f,
                target = 130f,
                unit = "g"
            )
            MacroNutrientsNeed(
                label = "PROTEIN",
                current = 90f,
                target = 130f,
                unit = "g"
            )
        }
    }
}