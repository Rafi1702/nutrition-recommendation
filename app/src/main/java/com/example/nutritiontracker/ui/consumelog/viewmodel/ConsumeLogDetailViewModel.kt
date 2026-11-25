package com.example.nutritiontracker.ui.consumelog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.usecase.consumelog.GetConsumeLogUseCase
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.utils.nowYyyyMMDD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PersonalConsumeLogsFilter(
    val selectedTimeFilterValues: Int = 0,
    val selectedMealTypes: List<MealType> = listOf(MealType.UNSPECIFIED)
)

@HiltViewModel
class ConsumeLogDetailViewModel @Inject constructor(
    getConsumeLogUseCase: GetConsumeLogUseCase,
) : ViewModel() {

    private val _personalConsumeLogFilterState = MutableStateFlow(PersonalConsumeLogsFilter())
    val personalConsumeLogsFilter = _personalConsumeLogFilterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val consumeLogsFlow = _personalConsumeLogFilterState
        .flatMapLatest { filter ->
            getConsumeLogUseCase(
                mealType = filter.selectedMealTypes.first(),
                consumedAt = nowYyyyMMDD()
            ).map { pagingData ->
                pagingData.map { consumeLog ->
                    ConsumeLogUiState(consumeLog = consumeLog)
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