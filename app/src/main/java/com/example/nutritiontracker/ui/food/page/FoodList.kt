package com.example.nutritiontracker.ui.food.page

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.components.MultiSelectOptions
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
    }) { selectableItems ->
        FoodList(
            foodItems = viewModel.foodsFlow,
            selectedFoodIds = selectableItems.toList().map { it.toInt() },
            onAddFoodToConsumeLogs = { food, mealType ->
                viewModel.addFoodToConsumeLog(food, mealType)
            }
        )
    }
}


@Composable
fun FoodListMealSelections(
    onMealsChanged: (List<MealType>) -> Unit = {},
) {
    MultiSelectOptions(
        items = MealType.entries,
        onValuesChanged = onMealsChanged
    ) { mealType, selected ->
        if (mealType == MealType.UNSPECIFIED) return@MultiSelectOptions

        SelectionChip(
            selected = selected
        ) {
            Text(mealType.label)
        }
    }
}

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    foodItems: Flow<PagingData<FoodUiState>>,
    selectedFoodIds: List<Int> = emptyList(),
    onAddFoodToConsumeLogs: (food: Food, mealType: MealType) -> Unit = { _, _ -> },
) {
    val selectedMealType = remember { mutableStateMapOf<MealType, List<Int>>() }
    val foodLazyItems = foodItems.collectAsLazyPagingItems()

    ListPagingSection(
        modifier = modifier
            .fillMaxSize(),
        paddingValues = PaddingValues.Zero,
        pagingItems = foodLazyItems,
        itemKey = { uiState -> uiState.food.id },
        headerProps = (false to {
            FoodListMealSelections(
                onMealsChanged = { meals ->
                    if (meals.isEmpty()) {
                        selectedMealType.clear()
                    } else {
                        selectedMealType.clear()
                        selectedMealType.putAll(meals.associateWith { selectedFoodIds })
                    }
                }
            )
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
                        onAddFoodToConsumeLogs(uiState.food, MealType.UNSPECIFIED)
                    }
                }
            )
        }
    }
}

