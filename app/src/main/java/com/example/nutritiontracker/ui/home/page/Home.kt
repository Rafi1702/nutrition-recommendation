package com.example.nutritiontracker.ui.home.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.domain.MacroType
import com.example.nutritiontracker.ui.home.components.FoodCard
import com.example.nutritiontracker.ui.home.components.MacroNutrientsNeed
import com.example.nutritiontracker.ui.home.components.UserNeedsCard
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.Debounce

@Preview
@Composable
internal fun Home(modifier: Modifier = Modifier) {
    var recommendationFoods by remember { mutableStateOf(MOCK_FOODS) }

    Surface(
        modifier = modifier,
        color = colorScheme.surface
    ) {
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
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
}


private fun LazyListScope.recommendedFoodSection(recommendationFoods: List<Food> = emptyList()) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.food_recommendation_list))
        Spacer(modifier = Modifier.height(8.dp))
    }
    itemsIndexed(recommendationFoods, key = { _, item -> item.id }) { index, food ->
        if (index > 0) {
            Spacer(modifier = Modifier.height(8.dp))
        }
        FoodCard(food)
    }
}

@Preview
@Composable
private fun UserNeedSection(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.background(color = colorScheme.surface)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Makro Nutrisi Hari Ini", style = typography.labelLarge)
            Text(
                "Target gram",
                style = typography.labelSmall.copy(color = LocalContentColor.current.copy(alpha = .5f))
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            MacroNutrientsNeed(
                modifier = Modifier.weight(1f),
                label = MacroType.PROTEIN,
                current = 90f,
                target = 130f,
                unit = "g"
            )
            MacroNutrientsNeed(
                modifier = Modifier.weight(1f),
                label = MacroType.FAT,
                current = 90f,
                target = 130f,
                unit = "g"
            )
            MacroNutrientsNeed(
                modifier = Modifier.weight(1f),
                label = MacroType.CARBS,
                current = 90f,
                target = 130f,
                unit = "g"
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF808080
)
@Composable
private fun FoodCardPreview() {
    FoodCard(food = MOCK_FOODS[0])
}

@Preview
@Composable
private fun UserCardPreview() {
    UserNeedsCard()
}
