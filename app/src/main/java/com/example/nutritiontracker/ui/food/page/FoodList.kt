package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.nutritiontracker.ui.theme.LocalSnackBarHost
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.utils.composable.MutationEffect
import com.example.nutritiontracker.ui.utils.composable.MutationState
import kotlinx.coroutines.flow.Flow

@Composable
fun FoodListPage(modifier: Modifier = Modifier, viewModel: FoodListViewModel = hiltViewModel()) {

    val selectedMealType = remember { mutableStateMapOf<MealType, List<Int>>() }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarState = LocalSnackBarHost.current
    MutationEffect(
        mutationState = uiState.value,
        onFailure = { err ->
            snackBarState.showSnackbar(message = err.message ?: "Unknown")
        },
        onSuccess = {},
        coroutineScope = rememberCoroutineScope()
    )

    SelectableItemsWrapper(onFloatingButtonClicked = { ids ->
        val selectableItemsId = ids.toList().map { it.toInt() }

        selectedMealType.keys.toList().forEach { key ->
            selectedMealType[key] = selectableItemsId
        }
        viewModel.addAllFoodsToConsumeLog(consumeMeals = selectedMealType)
    }, isLoading = uiState.value is MutationState.Pending) { selectableItems ->

        val selectableItemsId = selectableItems.toList().map { it.toInt() }
        FoodList(
            foodItems = viewModel.foodsFlow,
            onSelectableModeItemClick = {
                selectedMealType.keys.toList().forEach { key ->
                    selectedMealType[key] = selectableItemsId
                }
            },
            onMealsChanged = { meals ->
                if (meals.isEmpty()) {
                    selectedMealType.clear()
                } else {
                    selectedMealType.clear()
                    selectedMealType.putAll(meals.associateWith {
                        selectableItemsId
                    })
                }
            },
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
    onMealsChanged: (List<MealType>) -> Unit = {},
    onSelectableModeItemClick: (Int) -> Unit = {},
    onAddFoodToConsumeLogs: (food: Food, mealType: MealType) -> Unit = { _, _ -> },
) {
    val foodLazyItems = foodItems.collectAsLazyPagingItems()

    ListPagingSection(
        modifier = modifier
            .fillMaxSize(),
        paddingValues = PaddingValues.Zero,
        pagingItems = foodLazyItems,
        itemKey = { uiState -> uiState.food.id },
        headerProps = (false to {
            FoodListMealSelections(
                onMealsChanged = onMealsChanged
            )
        }),
        initialDataError = {
            Text("Initial Data Error")
        }) { uiState ->
        SelectableItem(
            itemId = uiState.food.id.toString(),
            onItemClick = {
                onSelectableModeItemClick(uiState.food.id)
            }
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

