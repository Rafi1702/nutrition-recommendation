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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MacroNutrients
import com.example.nutritiontracker.domain.model.MacroType
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.components.DatePicker
import com.example.nutritiontracker.ui.consumelog.components.ConsumeLogSurface
import com.example.nutritiontracker.ui.home.components.MacroNutrientsNeed
import com.example.nutritiontracker.ui.home.components.ShowMoreButton
import com.example.nutritiontracker.ui.home.components.UserNeedsCard
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


val mockConsumeLogs = listOf(
    ConsumeLog(
        id = 1,
        userId = 101,
        eatTime = "2026-10-19T08:00:00Z",
        mealType = MealType.BREAKFAST,
        food = Food(
            id = 1,
            name = "Nasi Goreng Spesial",
            macros = listOf(
                MacroNutrients(type = MacroType.PROTEIN, serveValue = 12.5), // Indeks 0
                MacroNutrients(type = MacroType.CARBS, serveValue = 45.0),   // Indeks 1
                MacroNutrients(type = MacroType.FAT, serveValue = 15.0)      // Indeks 2
            )
        )
    ),
    ConsumeLog(
        id = 2,
        userId = 101,
        eatTime = "2026-10-19T12:30:00Z",
        mealType = MealType.LUNCH,
        food = Food(
            id = 2,
            name = "Dada Ayam Bakar & Nasi Merah",
            macros = listOf(
                MacroNutrients(type = MacroType.PROTEIN, serveValue = 30.0),
                MacroNutrients(type = MacroType.CARBS, serveValue = 35.0),
                MacroNutrients(type = MacroType.FAT, serveValue = 5.5)
            )
        )
    ),
    ConsumeLog(
        id = 3,
        userId = 101,
        eatTime = "2026-10-19T19:00:00Z",
        mealType = MealType.DINNER,
        food = Food(
            id = 3,
            name = "Sup Ikan Kuah Kuning",
            macros = listOf(
                MacroNutrients(type = MacroType.PROTEIN, serveValue = 22.0),
                MacroNutrients(type = MacroType.CARBS, serveValue = 10.0),
                MacroNutrients(type = MacroType.FAT, serveValue = 8.0)
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
internal fun HomePage(
    modifier: Modifier = Modifier,
    onNavigateToConsumeLogDetail: () -> Unit = {},
    onNavigateToRecommendedFoodList: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = colorScheme.surface
    ) {
        LazyColumn(contentPadding = PaddingValues(horizontal = Spacing.m)) {
            item {
                DatePicker()
            }
            item {
                Spacer(modifier = Modifier.height(Spacing.m))
                UserNeedsCard()
            }

            item {
                Spacer(modifier = Modifier.height(Spacing.m))
                UserNeedSection()
            }

            item {
                Spacer(modifier = Modifier.height(Spacing.m))
                RecommendedFoodSection(onNavigateToRecommendedFoodList = onNavigateToRecommendedFoodList)
            }

            item {
                Spacer(modifier = Modifier.height(Spacing.m))
                ConsumeLogSurface(
                    consumeLog = mockConsumeLogs,
                    onNavigateToConsumeLogDetail = onNavigateToConsumeLogDetail
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecommendedFoodSection(
    modifier: Modifier = Modifier,
    onNavigateToRecommendedFoodList: () -> Unit = {}
) {

    Row(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.weight(.6f)
        ) {
            Text(
                stringResource(R.string.food_recommendation_section_home),
                style = typography.titleMedium.copy(colorScheme.onSurface)
            )
            Text(
                stringResource(R.string.food_recommendation_section_home_sub),
                style = typography.titleSmall.copy(colorScheme.onSurface.copy(alpha = .5f))
            )
        }
        ShowMoreButton(minimumInteractive = true, onClick = onNavigateToRecommendedFoodList)
    }
}

@Preview
@Composable
private fun UserNeedSection(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.s),
        modifier = Modifier.background(color = colorScheme.surface)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.today_macro_nutrition_needs),
                style = typography.labelLarge.copy(color = colorScheme.onSurface)
            )
            Text(
                stringResource(R.string.target),
                style = typography.labelSmall.copy(color = LocalContentColor.current.copy(alpha = .5f))
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = Spacing.s)
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
