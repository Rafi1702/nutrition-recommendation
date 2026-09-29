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
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.MacroType
import com.example.nutritiontracker.ui.components.DatePicker
import com.example.nutritiontracker.ui.home.components.ConsumeLogSurface
import com.example.nutritiontracker.ui.home.components.MacroNutrientsNeed
import com.example.nutritiontracker.ui.home.components.ShowMoreButton
import com.example.nutritiontracker.ui.home.components.UserNeedsCard
import com.example.nutritiontracker.ui.theme.LocalNavController
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

@Preview(showBackground = true)
@Composable
internal fun HomePage(modifier: Modifier = Modifier) {
    val navController = LocalNavController.current

    Surface(
        modifier = modifier,
        color = colorScheme.surface
    ) {
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
            item {
                DatePicker()
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                UserNeedsCard()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                UserNeedSection()
            }

            if (navController != null) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    RecommendedFoodSection(onSeeMoreClick = {
                        navController.navigate(HomePath.FOOD_RECOMMENDATIONS)
                    })
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ConsumeLogSurface()
            }
        }
    }
}

@Preview
@Composable
private fun RecommendedFoodSection(
    modifier: Modifier = Modifier,
    onSeeMoreClick: (() -> Unit)? = null
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

        onSeeMoreClick?.let {
            ShowMoreButton(minimumInteractive = true, onClick = onSeeMoreClick)
        }
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
