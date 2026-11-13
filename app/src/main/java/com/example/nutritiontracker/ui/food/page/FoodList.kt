package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.components.MultiSelectOptions
import com.example.nutritiontracker.ui.components.SelectableItem
import com.example.nutritiontracker.ui.components.SelectableItemsWrapper
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.components.SelectionMode
import com.example.nutritiontracker.ui.components.label
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.theme.LocalSnackBarHost
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.utils.composable.MutationEffect
import com.example.nutritiontracker.ui.utils.composable.MutationState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListPage(modifier: Modifier = Modifier, viewModel: FoodListViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val selectedMealType = remember { mutableStateMapOf<MealType, List<Int>>() }
    val selectionMode = remember { mutableStateOf(SelectionMode.SINGLE) }
    val showBottomSheet = remember { mutableStateOf(false) }

    val snackBarState = LocalSnackBarHost.current
    val scope = rememberCoroutineScope()

    MutationEffect(
        mutationState = uiState.value,
        onFailure = { err ->
            snackBarState.showSnackbar(message = err.message ?: "Unknown")
        },
        onSuccess = {},
        coroutineScope = scope
    )

    SelectableItemsWrapper(
        selectionMode = selectionMode.value,
        floatingButton = { ids ->
            Button(onClick = {
                showBottomSheet.value = true
            }) {
                Text(stringResource(R.string.see_selected_items))
            }
        },
        header = {
            val expanded = remember { mutableStateOf(false) }

            Box {
                Text(selectionMode.value.label, modifier = Modifier.clickable {
                    expanded.value = true
                })
                DropdownMenu(expanded = expanded.value, {
                    expanded.value = false
                }) {
                    DropdownMenuItem(
                        text = { Text(stringArrayResource(R.array.selection_mode_array)[0]) },
                        onClick = {
                            selectionMode.value = SelectionMode.SINGLE
                            expanded.value = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringArrayResource(R.array.selection_mode_array)[1]) },
                        onClick = {
                            selectionMode.value = SelectionMode.BULK
                            expanded.value = false
                        }
                    )
                }
            }

        }) { selectableItems ->

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

        if (showBottomSheet.value) ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
        ) {
            Column(modifier = Modifier.background(color = Color.Red)) {
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                ) {
                    val filteredFoods =
                        viewModel.availableFoods.filter { food -> selectableItems.contains(food.id.toString()) }

                    items(filteredFoods) { food ->
                        if (selectableItems.contains(food.id.toString())) FoodCard(
                            food = food
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(color = colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = uiState.value is MutationState.Pending,
                        onClick = {
                            val selectableItemsId = selectableItems.toList().map { it.toInt() }

                            selectedMealType.keys.toList().forEach { key ->
                                selectedMealType[key] = selectableItemsId
                            }
                            viewModel.addAllFoodsToConsumeLog(consumeMeals = selectedMealType)
                        }) {
                        when (uiState.value) {
                            is MutationState.Pending -> {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }

                            else -> {
                                Text(stringResource(R.string.add_foods_to_consume_logs))
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "add_all_foods_to_consume_logs"
                                )
                            }
                        }
                    }
                }
            }
        }
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
            Text(stringResource(R.string.initial_data_error))
        }) { uiState ->
        SelectableItem(
            itemId = uiState.food.id.toString(),
            enableLongClick = false,
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
