package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.components.SelectableItem
import com.example.nutritiontracker.ui.components.SelectableItemsWrapper
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.theme.Spacing
import kotlinx.coroutines.flow.Flow

@Composable
fun FoodListPage(modifier: Modifier = Modifier, viewModel: FoodListViewModel = hiltViewModel()) {

    SelectableItemsWrapper(onFloatingButtonClicked = { ids ->
//        viewModel.addAllFoodsToConsumeLog(ids)
    }) {
        FoodList(
            foodItems = viewModel.foodsFlow,
            onAddFoodToConsumeLogs = { food, mealType ->
                viewModel.addFoodToConsumeLog(food, mealType)
            }
        )
    }
}


@Composable
fun FoodListMealSelections(
    selectedMealType: MealType = MealType.BREAKFAST,
    onSelectionMealTypeChanged: (MealType) -> Unit = {}
) {
    Row(
        modifier = Modifier.padding(Spacing.s),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        MealType.entries.forEach { mealType ->
            if (mealType == MealType.UNSPECIFIED) return@forEach

            SelectionChip(
                modifier = Modifier.clickable {
                    onSelectionMealTypeChanged(mealType)
                },
                selected = selectedMealType == mealType
            ) {
                Text(mealType.label)
            }
        }
    }

}

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    foodItems: Flow<PagingData<FoodUiState>>,
    onAddFoodToConsumeLogs: (food: Food, mealType: MealType) -> Unit = { _, _ -> },
) {
    val selectedMealType = remember { mutableStateOf(MealType.BREAKFAST) }
    val foodLazyItems = foodItems.collectAsLazyPagingItems()
    ListPagingSection(
        modifier = modifier
            .fillMaxSize(),
        paddingValues = PaddingValues.Zero,
        pagingItems = foodLazyItems,
        itemKey = { uiState -> uiState.food.id },
        headerProps = (false to {
            FoodListMealSelections(
                selectedMealType = selectedMealType.value,
                onSelectionMealTypeChanged = { newMealType ->
                    selectedMealType.value = newMealType
                })
        }),
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
                        onAddFoodToConsumeLogs(uiState.food, selectedMealType.value)
                    }
                }
            )
        }
    }
}

