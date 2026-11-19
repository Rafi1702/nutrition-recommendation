package com.example.nutritiontracker.ui.food.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.example.nutritiontracker.data.mocks.MOCK_FOODS
import com.example.nutritiontracker.ui.components.SelectableItemsWrapper
import com.example.nutritiontracker.ui.components.SelectionMode
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Preview(showBackground = true)
@Composable
fun FoodListPageHeaderPreview() {
    NutritionTrackerTheme {
        FoodListMealSelections(selectedMeals = emptyList())
    }
}

@Preview(showBackground = true)
@Composable
fun FoodListPreview() {
    val pagingData = PagingData.from(MOCK_FOODS.map { FoodUiState(food = it, isLoading = false) })
    val fakeDataFlow = MutableStateFlow(pagingData)
    NutritionTrackerTheme {
        SelectableItemsWrapper { selectableState ->
            FoodList(
                foodItems = fakeDataFlow,
                selectableState = selectableState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodListPreviewBulk() {
    val pagingData = PagingData.from(MOCK_FOODS.map { FoodUiState(food = it, isLoading = false) })
    val fakeDataFlow = MutableStateFlow(pagingData)
    NutritionTrackerTheme {
        SelectableItemsWrapper(selectionMode = SelectionMode.BULK) { selectableState ->
            FoodList(
                foodItems = fakeDataFlow,
                selectableState = selectableState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectionModeDropdownPreview() {
    NutritionTrackerTheme {
        SelectionModeDropdown(selectionMode = SelectionMode.SINGLE) {}
    }
}
