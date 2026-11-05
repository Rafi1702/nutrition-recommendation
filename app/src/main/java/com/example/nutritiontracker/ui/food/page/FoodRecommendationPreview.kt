package com.example.nutritiontracker.ui.food.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.example.nutritiontracker.data.mocks.mockRecommendationFoods
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Preview
@Composable
internal fun FoodRecommendationsPreview() {
    val pagingData = PagingData.from(mockRecommendationFoods)
    val fakeDataFlow = MutableStateFlow(pagingData)

    NutritionTrackerTheme {
        FoodRecommendationsContent(foodRecommendation = fakeDataFlow)
    }
}
