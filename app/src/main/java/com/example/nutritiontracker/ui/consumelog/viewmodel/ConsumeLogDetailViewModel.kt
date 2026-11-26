package com.example.nutritiontracker.ui.consumelog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.usecase.consumelog.GetConsumeLogUseCase
import com.example.nutritiontracker.domain.usecase.consumelog.GetPersonalConsumeLogFlowUseCase
import com.example.nutritiontracker.domain.usecase.consumelog.GetPersonalGroupedConsumeLogUseCase
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.utils.DateRange
import com.example.nutritiontracker.ui.utils.currentLocalDate
import com.example.nutritiontracker.ui.utils.daysAfter
import com.example.nutritiontracker.ui.utils.daysAgo
import com.example.nutritiontracker.ui.utils.formatWith
import com.example.nutritiontracker.ui.utils.rangeToFormattedRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

internal val TimeRangeFilterInDays = listOf(30, 15, 1800)

data class PersonalConsumeLogsFilter(
    val selectedTimeFilterValues: Int = TimeRangeFilterInDays.first(),
    val selectedMealTypes: List<MealType> = listOf(MealType.UNSPECIFIED)
) {
//    @OptIn(ExperimentalTime::class)
//    val dateRange: DateRange
//        get() = DateRange(
//            currentLocalDate().formatWith(),
//            daysAfter(selectedTimeFilterValues).formatWith()
//        )

    @OptIn(ExperimentalTime::class)
    val dateRange: DateRange
        get() = DateRange(
            "2026-10-27",
            "2026-11-26"
        )
}

@HiltViewModel
class ConsumeLogDetailViewModel @Inject constructor(
    getPersonalConsumeLogFlowUseCase: GetPersonalGroupedConsumeLogUseCase
) : ViewModel() {

    private val _personalConsumeLogFilterState = MutableStateFlow(PersonalConsumeLogsFilter())
    val personalConsumeLogsFilter = _personalConsumeLogFilterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    val consumeLogsFlow = _personalConsumeLogFilterState
        .flatMapLatest { filter ->
            getPersonalConsumeLogFlowUseCase(
                mealType = filter.selectedMealTypes.first(),
                startDate = filter.dateRange.startDate,
                endDate = filter.dateRange.endDate,
                groupBy = "consumed_at"
            ).map { pagingData ->
                pagingData.map { groupedConsumeLog ->
                    ConsumeLogUiState(consumeLogs = groupedConsumeLog.consumes)
                }
            }
        }
        .cachedIn(viewModelScope)

    fun onSelectedMealTypesChange(
        mealTypes: List<MealType>
    ) {
        viewModelScope.launch {
            _personalConsumeLogFilterState.update { it.copy(selectedMealTypes = mealTypes) }
        }
    }

    fun onSelectedTimeFilterValues(timeRange: Int) {
        _personalConsumeLogFilterState.update { it.copy(selectedTimeFilterValues = timeRange) }
    }
}