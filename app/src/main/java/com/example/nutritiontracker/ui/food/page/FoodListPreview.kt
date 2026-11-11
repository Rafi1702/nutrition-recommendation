package com.example.nutritiontracker.ui.food.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.example.nutritiontracker.data.mocks.MOCK_FOODS
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.components.SelectableItemsWrapper
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Preview(showBackground = true)
@Composable
fun FoodListPageHeader(){
    FoodListMealSelections({})
}

@Preview(showBackground = true)
@Composable
fun FoodListPreview() {
    val pagingData = PagingData.from(MOCK_FOODS.map { FoodUiState(food = it, isLoading = false) })
    val fakeDataFlow = MutableStateFlow(pagingData)
    NutritionTrackerTheme {
        SelectableItemsWrapper { FoodList(foodItems = fakeDataFlow) }
    }
}