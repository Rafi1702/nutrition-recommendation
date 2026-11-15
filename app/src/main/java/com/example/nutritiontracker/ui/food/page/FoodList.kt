package com.example.nutritiontracker.ui.food.page

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.composable.MutationEffect
import com.example.nutritiontracker.ui.utils.composable.MutationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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
            Button(
                onClick = { showBottomSheet.value = true },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = Spacing.m)
            ) {
                Text(stringResource(R.string.see_selected_items))
            }
        },
        header = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.m, vertical = Spacing.xs),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Selection Mode",
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    )
                    Text(
                        text = stringResource(R.string.food_list_subtitle),
                        style = typography.labelSmall.copy(
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }

                SelectionModeDropdown(
                    selectionMode = selectionMode.value
                ) { newMode ->
                    selectionMode.value = newMode
                }
            }
        }) { selectableItems ->

        val selectableItemsId = selectableItems.toList().map { it.toInt() }
        FoodList(
            modifier = modifier,
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

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.m, vertical = Spacing.s),
                    verticalArrangement = Arrangement.spacedBy(Spacing.m)
                ) {
                    Text(
                        text = stringResource(R.string.see_selected_items),
                        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    val filteredFoods = viewModel.availableFoods.filter { food ->
                        selectableItems.contains(food.id.toString())
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(Spacing.s)
                    ) {
                        items(filteredFoods) { food ->
                            FoodCard(food = food)
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = uiState.value !is MutationState.Pending,
                        onClick = {
                            val selectableItemsId = selectableItems.toList().map { it.toInt() }

                            selectedMealType.keys.toList().forEach { key ->
                                selectedMealType[key] = selectableItemsId
                            }
                            viewModel.addAllFoodsToConsumeLog(consumeMeals = selectedMealType)
                        }
                    ) {
                        if (uiState.value is MutationState.Pending) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text(stringResource(R.string.add_foods_to_consume_logs))
                            Spacer(modifier = Modifier.width(Spacing.xs))
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

@Composable
fun FoodListMealSelections(
    modifier: Modifier = Modifier,
    onMealsChanged: (List<MealType>) -> Unit = {},
) {
    MultiSelectOptions(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.xs),
        items = MealType.entries.filter { it != MealType.UNSPECIFIED },
        onValuesChanged = onMealsChanged
    ) { mealType, selected ->
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
        modifier = modifier.fillMaxSize(),
        paddingValues = PaddingValues(bottom = Spacing.m),
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

@Composable
internal fun SelectionModeDropdown(
    selectionMode: SelectionMode,
    modifier: Modifier = Modifier,
    onSelectionModeChange: (SelectionMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Surface(
        onClick = {
            scope.launch {
                rotation.animateTo(
                    targetValue = 180f,
                    animationSpec = tween(100, easing = LinearEasing)
                )
            }
            expanded = true
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.s),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = colorScheme.primary
            )
            Text(
                text = selectionMode.label,
                modifier = Modifier.width(48.dp),
                style = typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "selection_mode_dropdown_icon",
                modifier = Modifier
                    .size(18.dp)
                    .rotate(rotation.value)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                scope.launch {
                    rotation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(100, easing = LinearEasing)
                    )
                }
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = { Text(stringArrayResource(R.array.selection_mode_array)[0]) },
                onClick = {
                    onSelectionModeChange(SelectionMode.SINGLE)
                    scope.launch {
                        rotation.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(100, easing = LinearEasing)
                        )
                    }
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringArrayResource(R.array.selection_mode_array)[1]) },
                onClick = {
                    onSelectionModeChange(SelectionMode.BULK)
                    scope.launch {
                        rotation.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(100, easing = LinearEasing)
                        )
                    }
                    expanded = false
                }
            )
        }
    }
}
