package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.example.nutritiontracker.data.mocks.MOCK_CONSUME_LOGS
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.consumelog.viewmodel.TimeRangeFilterInDays
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.utils.currentLocalDate
import com.example.nutritiontracker.ui.utils.formatWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.ExperimentalTime

@Preview(showBackground = true)
@Composable
private fun PersonalConsumeListPreview() {
    val pagingData = PagingData.from(MOCK_CONSUME_LOGS.map { item ->
        ConsumeLogUiState(
            consumeLog = item,
            isLoading = false
        )
    })
    val fakeDataFlow = MutableStateFlow(pagingData)
    NutritionTrackerTheme {
        PersonalConsumeList(
            innerPadding = PaddingValues(horizontal = 16.dp),
            consumeLogs = fakeDataFlow,
            selectedFilterValues = TimeRangeFilterInDays.first(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsumeLogDetailHeaderPreview() {
    NutritionTrackerTheme {
        ConsumeLogHeader()
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun ConsumeLogItemPreview() {
    val dateFormat = currentLocalDate().formatWith()
    val dateDisplay = dateFormat.split("-")

    if (dateDisplay.isNotEmpty()) {
        ConsumeLogItem(
            date = "${dateDisplay[2]} ${
                dateDisplay[1].substring(
                    0,
                    3,
                )
            } ${dateDisplay[0]}", consumes = MOCK_CONSUME_LOGS,
            leadingText = "Hari"
        )
    }
}


