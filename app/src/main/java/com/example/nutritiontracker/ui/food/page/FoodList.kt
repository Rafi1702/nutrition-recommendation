package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.components.SelectableItem
import com.example.nutritiontracker.ui.components.SelectableItemsWrapper
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.theme.Spacing

@Composable
fun FoodListPage(modifier: Modifier = Modifier, viewModel: FoodListViewModel = hiltViewModel()) {
    val lazyFoodItems = viewModel.foodsFlow.collectAsLazyPagingItems()
    SelectableItemsWrapper(onFloatingButtonClicked = { ids ->
//        viewModel.addAllFoodsToConsumeLog(ids)
    }) {
        ListPagingSection(
            modifier = modifier
                .fillMaxSize(),
            paddingValues = PaddingValues.Zero,
            pagingItems = lazyFoodItems,
            itemKey = { uiState -> uiState.food.id },
            initialDataError = {
                Text("Initial Data Error")
            }) { uiState ->
            SelectableItem(
                itemId = uiState.food.id.toString(),
            ) { isSelected ->
                FoodCard(
                    modifier = Modifier.padding(horizontal = Spacing.s),
                    food = uiState.food,
                    isLoading = uiState.isLoading,
                    onAddClick = if (isSelected) {
                        null
                    } else {
                        {
                                viewModel.addFoodToConsumeLog(uiState.food)
                        }
                    }
                )
            }
        }
    }
}
