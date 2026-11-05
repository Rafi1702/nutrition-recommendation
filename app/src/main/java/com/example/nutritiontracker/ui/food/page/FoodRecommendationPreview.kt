package com.example.nutritiontracker.ui.food.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme

@Preview
@Composable
internal fun FoodRecommendationsPreview() {
//    val fakeRecommendationFood = List(10){
//        RecommendationFood(
//
//        )
//    }
    NutritionTrackerTheme {
        FoodRecommendationsPage()
    }
}
