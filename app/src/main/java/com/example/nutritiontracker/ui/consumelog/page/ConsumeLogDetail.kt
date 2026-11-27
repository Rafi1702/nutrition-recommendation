package com.example.nutritiontracker.ui.consumelog.page

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.model.NutrientsSubTotal
import com.example.nutritiontracker.domain.model.nutrientsSubTotal
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.Dropdown
import com.example.nutritiontracker.ui.components.DropdownItemProps
import com.example.nutritiontracker.ui.components.FilledChipStyle
import com.example.nutritiontracker.ui.components.LoadingLottieAnimation
import com.example.nutritiontracker.ui.components.MultiSelectOptions
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.components.defaultColor
import com.example.nutritiontracker.ui.consumelog.components.properties
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.consumelog.viewmodel.ConsumeLogDetailViewModel
import com.example.nutritiontracker.ui.consumelog.viewmodel.TimeRangeFilterInDays
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.composable.plus
import com.example.nutritiontracker.ui.utils.composable.withStyle
import com.example.nutritiontracker.ui.utils.formatCurrentDateWithContext
import com.example.nutritiontracker.ui.utils.toLocalDate
import kotlinx.coroutines.flow.Flow

@Composable
fun String.consumeLogHeaderDisplay(context: Context = LocalContext.current): String? {
    if (this.isBlank()) return null
    return try {
        this.toLocalDate().formatCurrentDateWithContext(context)
    } catch (_: Exception) {
        null
    }
}

private const val TOTAL_DAY_IN_MONTH = 30

fun Int.text(): String {
    //TODO: Replace the Time Unit (day/month/year) with Resource String
    return when {
        this > 365 -> "${this / 365} Year"
        this > TOTAL_DAY_IN_MONTH -> "${this / TOTAL_DAY_IN_MONTH} Month"
        else -> "$this Day"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumeLogDetail(
    viewModel: ConsumeLogDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val personalConsumeLogsFilter =
        viewModel.personalConsumeLogsFilter.collectAsStateWithLifecycle()

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        ConsumeLogHeader(
            modifier = Modifier.padding(Spacing.m),
            selectedFilterValues = personalConsumeLogsFilter.value.selectedTimeFilterValues.text(),
            onTimeRangeFilterChange = { updatedValue ->
                viewModel.onSelectedTimeFilterValues(updatedValue)
            },
            onSelectedMealTypeValuesChange = { updatedMealTypes ->
                if (updatedMealTypes.isEmpty()) return@ConsumeLogHeader

                viewModel.onSelectedMealTypesChange(updatedMealTypes)
            }
        )
        PersonalConsumeList(
            consumeLogs = viewModel.consumeLogsFlow,
            selectedFilterValues = personalConsumeLogsFilter.value.selectedTimeFilterValues
        )
    }
}

@Composable
internal fun ConsumeLogHeader(
    modifier: Modifier = Modifier,
    selectedFilterValues: String = TimeRangeFilterInDays.first().text(),
    selectedMealTypeValues: List<MealType> = emptyList(),
    onSelectedMealTypeValuesChange: (List<MealType>) -> Unit = {},
    onTimeRangeFilterChange: (Int) -> Unit = {}
) {

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "A complete record of your daily nutritional intake",
                style = typography.titleSmall,
                modifier = Modifier.weight(1.5f)
            )
            Dropdown(
                modifier = Modifier.weight(1f),
                selectedLabel = selectedFilterValues,
                items = TimeRangeFilterInDays.map { day ->
                    DropdownItemProps(
                        itemLabel = day.text(),
                        onClick = {
                            onTimeRangeFilterChange(day)
                        }
                    )
                },
                prefixIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "date_filter_icon")
                }
            )
        }
        MultiSelectOptions(
            items = MealType.entries,
            initialSelectedValues = selectedMealTypeValues,
            onValuesChanged = onSelectedMealTypeValuesChange,
            scrollable = true,
        ) { mealType, selected ->
            val defaultSelectedChipColor = FilledChipStyle.defaultColor()
            val mealsProperties = mealType.properties()
            val selectedChipColor = when (mealType) {
                MealType.UNSPECIFIED -> defaultSelectedChipColor
                else -> defaultSelectedChipColor.copy(
                    backgroundColor = mealsProperties.containerColor,
                    contentColor = mealsProperties.contentColor,
                )
            }
            SelectionChip(
                selected = selected,
                radius = 16.dp,
                selectedColors = selectedChipColor,
                prefixIcon = {
                    Icon(mealsProperties.icon ?: Icons.Default.Pending, contentDescription = null)
                }
            ) {
                Text(if (mealType == MealType.UNSPECIFIED) "All" else mealType.label)
            }
        }
    }
}

@Composable
internal fun PersonalConsumeList(
    innerPadding: PaddingValues = PaddingValues.Zero,
    selectedFilterValues: Int,
    consumeLogs: Flow<PagingData<ConsumeLogUiState>>
) {
    val consumeLogLazyItems = consumeLogs.collectAsLazyPagingItems()

    ListPagingSection(
        modifier = Modifier.fillMaxSize(),
        paddingValues = innerPadding + PaddingValues(Spacing.s),
        pagingItems = consumeLogLazyItems,
        itemKey = { consumeLogStatus -> consumeLogStatus.id},
        loadingContent = {
            LoadingLottieAnimation(resId = R.raw.food_loading)
        },
        initialDataError = {
            Text("Initial Data Error")
        }) { index, consumeLog ->
        val consumeLogs = consumeLog.groupedConsumeLog
        val leadingItemText =
            "Hari ${if (index > 0) "ke - ${selectedFilterValues - index}" else "ini"}"

        ConsumeLogItem(
            date = consumeLogs.consumedAt.consumeLogHeaderDisplay(),
            consumes = consumeLogs.consumes,
            leadingText = leadingItemText
        )
    }
}

@Composable
internal fun ConsumeLogItem(
    date: String?,
    consumes: List<ConsumeLog>,
    leadingText: String,
    nutrients: NutrientsSubTotal = consumes.nutrientsSubTotal()
) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.primary
                ) {
                    append("$leadingText\t\t")
                }
                date?.let {
                    withStyle(
                        style = typography.titleMedium,
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    ) {
                        append(date)
                    }
                }
            })

            Chip(
                colors = FilledChipStyle.defaultColor().copy(
                    backgroundColor = colorScheme.surfaceContainer,
                    contentColor = colorScheme.surfaceTint
                ),
                radius = 16.dp
            ) {
                Text(
                    "1000 / 2000 kkal",
                    style = typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
        consumes.forEach { consume ->
            val food = consume.consumeLogDetail.food
            FoodCard(food = food)
        }
    }
}





