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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.MacroType
import com.example.nutritiontracker.ui.components.DatePicker
import com.example.nutritiontracker.ui.consumelog.components.ConsumeLogSurface
import com.example.nutritiontracker.ui.home.components.MacroNutrientsNeed
import com.example.nutritiontracker.ui.home.components.ShowMoreButton
import com.example.nutritiontracker.ui.home.components.UserNeedsCard
import com.example.nutritiontracker.ui.home.viewmodel.HomeViewModel
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

@Preview(showBackground = true)
@Composable
internal fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onDateChanged: (String) -> Unit = {},
    onNavigateToConsumeLogDetail: () -> Unit = {},
    onNavigateToRecommendedFoodList: () -> Unit = {}
) {
    val uiState = viewModel.personalConsumeLogState.collectAsStateWithLifecycle()
    Surface(
        modifier = modifier,
        color = colorScheme.surface
    ) {
        LazyColumn(contentPadding = PaddingValues(horizontal = Spacing.m)) {
            item {
                DatePicker(onDateChanged = onDateChanged)
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
                    personalConsumeLogState = uiState.value,
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
