package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.model.NutrientsSubTotal
import com.example.nutritiontracker.domain.model.nutrientsSubTotal
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.ChipBorder
import com.example.nutritiontracker.ui.components.Dropdown
import com.example.nutritiontracker.ui.components.DropdownItemProps
import com.example.nutritiontracker.ui.components.FilledChipStyle
import com.example.nutritiontracker.ui.components.MultiSelectOptions
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.components.defaultColor
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.consumelog.viewmodel.ConsumeLogDetailViewModel
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.composable.plus
import com.example.nutritiontracker.ui.utils.composable.withStyle
import com.example.nutritiontracker.ui.utils.displayFormatter
import com.example.nutritiontracker.ui.utils.formatDate
import com.example.nutritiontracker.ui.utils.instant
import com.example.nutritiontracker.ui.utils.nowYyyyMMDD
import com.example.nutritiontracker.ui.utils.serviceFormatter
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


@Immutable
data class MealColorSet(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

@Immutable
data class MealColors(
    val breakfast: MealColorSet,
    val lunch: MealColorSet,
    val dinner: MealColorSet
) {
    operator fun get(type: MealType): MealColorSet? = when (type) {
        MealType.BREAKFAST -> breakfast
        MealType.LUNCH -> lunch
        MealType.DINNER -> dinner
        else -> null
    }
}

// ==========================================
// LIGHT THEME PALETTE
// ==========================================
val LightMealColors = MealColors(
    breakfast = MealColorSet(
        color = Color(0xFF8B5000),
        onColor = Color(0xFFFFFFFF),
        colorContainer = Color(0xFFFFDDB3),
        onColorContainer = Color(0xFF2C1600)
    ),
    lunch = MealColorSet(
        color = Color(0xFF006A60),
        onColor = Color(0xFFFFFFFF),
        colorContainer = Color(0xFF74F8E5),
        onColorContainer = Color(0xFF00201C)
    ),
    dinner = MealColorSet(
        color = Color(0xFF4C57A9),
        onColor = Color(0xFFFFFFFF),
        colorContainer = Color(0xFFDFE0FF),
        onColorContainer = Color(0xFF000D5F)
    )
)

// ==========================================
// DARK THEME PALETTE
// ==========================================
val DarkMealColors = MealColors(
    breakfast = MealColorSet(
        color = Color(0xFFFFB868),
        onColor = Color(0xFF4A2800),
        colorContainer = Color(0xFF6A3C00),
        onColorContainer = Color(0xFFFFDDB3)
    ),
    lunch = MealColorSet(
        color = Color(0xFF53DBC9),
        onColor = Color(0xFF003731),
        colorContainer = Color(0xFF005048),
        onColorContainer = Color(0xFF74F8E5)
    ),
    dinner = MealColorSet(
        color = Color(0xFFBCC2FF),
        onColor = Color(0xFF1B2678),
        colorContainer = Color(0xFF333F90),
        onColorContainer = Color(0xFFDFE0FF)
    )
)


//This only works for
fun LocalDate.consumeLogHeaderDisplay(): String? {
    val dateFormat = this.format(formatDate(displayFormatter))
    val dateDisplay = dateFormat.split("-")
    return if (dateDisplay.isNotEmpty()) "${dateDisplay[2]} ${
        dateDisplay[1].substring(
            0,
            3,
        )
    } ${dateDisplay[0]}" else null
}

val Boolean.mealColors: MealColors
    @Composable get() = if (this) DarkMealColors else LightMealColors


private const val TOTAL_DAY_IN_MONTH = 30

private fun Int.textDateDropdownLabel(): String {
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
    PersonalConsumeList(consumeLogs = viewModel.consumeLogsFlow)
}

@Composable
internal fun ConsumeLogHeader() {
    val filterValues = remember { listOf(30, 15) }
    var selectedFilterValues by remember {
        mutableStateOf(
            filterValues.first().textDateDropdownLabel()
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
            Text(
                "A complete record of your daily nutritional intake",
                style = typography.titleSmall,
                modifier = Modifier.weight(1.5f)
            )
            Dropdown(
                modifier = Modifier.weight(1f),
                selectedLabel = selectedFilterValues,
                items = filterValues.map { day ->
                    DropdownItemProps(
                        itemLabel = day.textDateDropdownLabel(),
                        onClick = {
                            selectedFilterValues = day.textDateDropdownLabel()
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
            initialSelectedValues = listOf(MealType.UNSPECIFIED),

            ) { mealType, selected ->
            val defaultSelectedChipColor = FilledChipStyle.defaultColor()
            val mealsColorSet = isSystemInDarkTheme().mealColors
            val selectedChipColor = when (mealType) {
                MealType.UNSPECIFIED -> defaultSelectedChipColor
                else -> {
                    val updatedChipColor = mealsColorSet[mealType]
                    defaultSelectedChipColor.copy(
                        backgroundColor = updatedChipColor?.colorContainer
                            ?: defaultSelectedChipColor.backgroundColor,
                        contentColor = updatedChipColor?.onColorContainer
                            ?: defaultSelectedChipColor.contentColor,
                        chipBorder = ChipBorder()
                    )
                }
            }
            SelectionChip(
                selected = selected,
                radius = 16.dp,
                selectedColors = selectedChipColor,
                prefixIcon = {
                    Icon(Icons.Default.Pending, contentDescription = null)
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
    consumeLogs: Flow<PagingData<ConsumeLogUiState>>
) {
    val consumeLogLazyItems = consumeLogs.collectAsLazyPagingItems()

    ListPagingSection(
        modifier = Modifier.fillMaxSize(),
        paddingValues = innerPadding + PaddingValues(vertical = Spacing.s),
        pagingItems = consumeLogLazyItems,
        headerProps = (false to {
            ConsumeLogHeader()
        }),
        itemKey = { consumeLogStatus -> consumeLogStatus.consumeLog.consumeLogDetail.id },
        initialDataError = {
            Text("Initial Data Error")
        }) { consumeLog ->
        ConsumeLogItem(
            date = instant.consumeLogHeaderDisplay(),
            consumes = listOf(consumeLog.consumeLog)
        )
    }
}

@Composable
internal fun ConsumeLogItem(
    date: String?,
    consumes: List<ConsumeLog>,
    nutrients: NutrientsSubTotal = consumes.nutrientsSubTotal()
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.primary
                ) {
                    append("Hari Ini\t\t")
                }
                date?.let {
                    withStyle(
                        style = typography.labelSmall,
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
        Column {

        }
    }
}





