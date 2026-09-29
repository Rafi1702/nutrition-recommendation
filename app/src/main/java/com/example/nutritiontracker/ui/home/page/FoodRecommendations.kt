package com.example.nutritiontracker.ui.home.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.ui.home.components.FoodCard

@Composable
internal fun FoodRecommendationsPage() {
    Scaffold { innerPadding ->
        FoodRecommendationsContent(paddingValues = innerPadding)
    }
}

@Preview(showBackground = true)
@Composable
internal fun FoodRecommendationsContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues.Zero
) {
    LaunchedEffect(Unit) {
        //TODO Call FoodRecommendation URL
    }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = paddingValues
    ) {
        items(MOCK_FOODS) { food ->
            FoodCard(food)
        }
    }
}