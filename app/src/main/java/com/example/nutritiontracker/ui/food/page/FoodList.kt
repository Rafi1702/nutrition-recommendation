package com.example.nutritiontracker.ui.food.page

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
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
import com.example.nutritiontracker.ui.components.SelectableState
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.components.SelectionMode
import com.example.nutritiontracker.ui.components.defaultCardElevation
import com.example.nutritiontracker.ui.components.label
import com.example.nutritiontracker.ui.components.rememberSelectableState
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.theme.LocalSnackBarHost
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.extended
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.composable.MutationEffect
import com.example.nutritiontracker.ui.utils.composable.MutationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListPage(
    modifier: Modifier = Modifier,
    viewModel: FoodListViewModel = hiltViewModel(),
    onSelectedFoodPicked: (Boolean) -> Unit = {}
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val selectedMealType = remember { mutableStateMapOf<MealType, List<Int>>() }
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectableState =
        rememberSelectableState(initialValue = emptySet<Food>())

    val snackBarState = LocalSnackBarHost.current

    DisposableEffect(Unit) {
        onDispose { selectableState.resetState() }
    }

    val addFoodsToConsumeLogSuccessMessage =
        stringResource(R.string.success_add_to_consume_logs_message)

    LaunchedEffect(Unit) {
        viewModel.eventMutation.collectLatest { mutationState ->
            when (mutationState) {
                is MutationState.Error -> {
                    if (selectableState.selectionMode == SelectionMode.SINGLE) {
                        snackBarState.showSnackbar(
                            message = mutationState.error.message ?: "Unknown"
                        )
                    }
                }

                is MutationState.Success<*> -> {
                    if (selectableState.selectionMode == SelectionMode.BULK) {
                        showBottomSheet = false
                        selectableState.resetState()
                    }
                    snackBarState.showSnackbar(addFoodsToConsumeLogSuccessMessage)
                }

                else -> return@collectLatest
            }
        }
    }

    SelectableItemsWrapper(
        initialState = selectableState,
        floatingButton = {
            Button(
                onClick = { showBottomSheet = true },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = Spacing.m)
            ) {
                Text(stringResource(R.string.see_selected_items))
            }
        },
    ) {
        LaunchedEffect(selectableState.isSelectableMode, selectableState.items) {
            onSelectedFoodPicked(selectableState.items.isNotEmpty() && selectableState.isSelectableMode)
        }

        val selectableItemsId = selectableState.items.toList().map { it.id }
        FoodList(
            modifier = modifier,
            foodItems = viewModel.foodsFlow,
            selectableState = selectableState,
            selectedMeals = selectedMealType.keys.toList(),
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
            onAddFoodToConsumeLogs = { food, _ ->
                viewModel.addFoodToConsumeLog(food, selectedMealType)
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
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
                        selectableState.items.contains(food)
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
                        enabled = uiState.value.second !is MutationState.Pending,
                        onClick = {
                            val selectableItemsId =
                                selectableState.items.toList().map { it.id }

                            selectedMealType.keys.toList().forEach { key ->
                                selectedMealType[key] = selectableItemsId
                            }
                            viewModel.addAllFoodsToConsumeLog(consumeMeals = selectedMealType)
                        }
                    ) {
                        if (uiState.value.second is MutationState.Pending) {
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

                    if (uiState.value.second is MutationState.Error) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                contentDescription = "bulk_mutation_error",
                                tint = colorScheme.extended.error
                            )
                            (uiState.value.second as MutationState.Error).error.message?.let {
                                Text(
                                    it,
                                    style = typography.labelMedium.copy(color = colorScheme.extended.error)
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
    modifier: Modifier = Modifier,
    selectedMeals: List<MealType> = emptyList(),
    onMealsChanged: (List<MealType>) -> Unit = {},
) {
    MultiSelectOptions(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.xs),
        items = MealType.entries.filter { it != MealType.UNSPECIFIED },
        initialSelectedValues = selectedMeals,
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
fun FoodListHeader(
    selectionMode: SelectionMode,
    onSelectionModeChanged: (SelectionMode) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.s),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            Surface(
                shape = CircleShape,
                color = colorScheme.primaryContainer,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = colorScheme.primary
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Selection Mode",
                    style = typography.titleSmall.copy(
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
        }

        SelectionModeDropdown(
            selectionMode = selectionMode,
            onSelectionModeChange = onSelectionModeChanged
        )
    }
}

@Composable
fun FoodListHeaderSection(
    selectionMode: SelectionMode,
    selectedMeals: List<MealType>,
    onSelectionModeChange: (SelectionMode) -> Unit,
    onMealsChanged: (List<MealType>) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.s, vertical = Spacing.xs),
        shape = RoundedCornerShape(16.dp),
        color = colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.xs),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
        ) {
            FoodListHeader(
                selectionMode = selectionMode,
                onSelectionModeChanged = onSelectionModeChange
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.m),
                thickness = 1.dp,
                color = colorScheme.outlineVariant.copy(alpha = 0.3f)
            )

            FoodListMealSelections(
                selectedMeals = selectedMeals,
                onMealsChanged = onMealsChanged
            )
        }
    }
}

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    foodItems: Flow<PagingData<FoodUiState>>,
    selectableState: SelectableState<Food>,
    selectedMeals: List<MealType> = emptyList(),
    selectedMealType: MealType = MealType.UNSPECIFIED,
    isHeaderSticky: Boolean = true,
    onMealsChanged: (List<MealType>) -> Unit = {},
    onSelectableModeItemClick: (Int) -> Unit = {},
    onAddFoodToConsumeLogs: (food: Food, mealType: MealType) -> Unit = { _, _ -> },
) {
    val foodLazyItems = foodItems.collectAsLazyPagingItems()

    ListPagingSection(
        modifier = modifier.fillMaxSize(),
        paddingValues = PaddingValues(bottom = Spacing.m),
        pagingItems = foodLazyItems,
        itemKey = { foodStateHolder -> foodStateHolder.food.id },
        headerProps = isHeaderSticky to {
            FoodListHeaderSection(
                selectionMode = selectableState.selectionMode,
                selectedMeals = selectedMeals,
                onSelectionModeChange = { newMode ->
                    selectableState.onSelectionModeChange(newMode)
                },
                onMealsChanged = onMealsChanged
            )
        },
        initialDataError = {
            Text(stringResource(R.string.initial_data_error))
        }) { _, foodStateHolder ->
        SelectableItem(
            item = foodStateHolder.food,
            selectableState = selectableState,
            enableLongClick = false,
            onItemClick = {
                onSelectableModeItemClick(foodStateHolder.food.id)
            }
        ) { _ ->
            FoodCard(
                modifier = Modifier.padding(horizontal = Spacing.s),
                food = foodStateHolder.food,
                isLoading = foodStateHolder.isLoading,
                onAddClick = if (selectableState.selectionMode == SelectionMode.BULK) {
                    null
                } else {
                    {
                        onAddFoodToConsumeLogs(foodStateHolder.food, selectedMealType)
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
    var surfaceWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = modifier.onSizeChanged { size ->
            surfaceWidth = with(density) { size.width.toDp() }
        }
    ) {
        Surface(
            onClick = {
                scope.launch {
                    rotation.animateTo(
                        targetValue = 180f,
                        animationSpec = tween(100, easing = LinearEasing)
                    )
                    expanded = true
                }
            },
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
        }

        DropdownMenu(
            modifier = Modifier
                .width(surfaceWidth)
                .background(colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
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
            com.example.nutritiontracker.ui.food.components.DropdownMenuItem(
                label = stringArrayResource(R.array.selection_mode_array)[0],
                selected = selectionMode == SelectionMode.SINGLE,
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

            com.example.nutritiontracker.ui.food.components.DropdownMenuItem(
                label = stringArrayResource(R.array.selection_mode_array)[1],
                selected = selectionMode == SelectionMode.BULK,
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


@Composable
private fun Modifier.selectedDropdownItemShape(selected: Boolean) = composed {
    if (selected) this.background(
        colorScheme.onSurfaceVariant,
        RoundedCornerShape(8.dp)
    ) else this
}
