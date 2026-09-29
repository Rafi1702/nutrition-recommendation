package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FoodRecommendationsPage(modifier: Modifier = Modifier) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "Food Recommendation",
                style = LocalTextStyle.current.copy(color = colorScheme.onSurface)
            )
        })
    }) { innerPadding ->
        FoodRecommendationsContent(
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
internal fun FoodRecommendationsPagePreview() {
    NutritionTrackerTheme {
        FoodRecommendationsPage()
    }
}


@Preview
@Composable
internal fun FoodRecommendationsContent(
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        //TODO Call FoodRecommendation URL
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.background(color = colorScheme.surface).padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(Icons.Default.Info, contentDescription = "food_recommendation_information")
                Text("Based on XXXX food is containing xx% carbs, xx% proteins, xx% fats")
            }
        }
        items(MOCK_FOODS) { food ->
            FoodCard(food)
        }
    }
}