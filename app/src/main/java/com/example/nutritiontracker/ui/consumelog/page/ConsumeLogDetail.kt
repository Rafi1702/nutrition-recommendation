package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.consumelog.viewmodel.ConsumeLogDetailViewModel
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.utils.composable.plus

import androidx.compose.ui.platform.LocalInspectionMode
import androidx.paging.PagingData
import com.example.nutritiontracker.ui.components.Dropdown
import com.example.nutritiontracker.ui.components.DropdownItemProps
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import kotlinx.coroutines.flow.Flow

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
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
            Text(
                "A complete record of your daily nutritional intake",
                style = typography.titleSmall,
                modifier = Modifier.weight(1.5f)
            )
            Dropdown(
                modifier = Modifier.weight(1f),
                items = emptyList(),
                selectedLabel = "Test",
                prefixIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "date_filter_icon")
                }
            )
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
        paddingValues = innerPadding + PaddingValues(horizontal = Spacing.m, vertical = Spacing.s),
        pagingItems = consumeLogLazyItems,
        headerProps = (false to {
            ConsumeLogHeader()
        }),
        itemKey = { consumeLogStatus -> consumeLogStatus.consumeLog.id },
        initialDataError = {
            Text("Initial Data Error")
        }) { consumeLog ->
        FoodCard(food = consumeLog.consumeLog.food)
    }
}





