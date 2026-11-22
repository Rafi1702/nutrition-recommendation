package com.example.nutritiontracker.ui.consumelog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.usecase.consumelog.GetConsumeLogUseCase
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject




@HiltViewModel
class ConsumeLogDetailViewModel @Inject constructor(
    getConsumeLogUseCase: GetConsumeLogUseCase,
) : ViewModel() {

    val consumeLogsFlow = getConsumeLogUseCase(mealType = MealType.BREAKFAST, consumedAt = "").map { consumeLogs ->
        consumeLogs.map { consumeLog ->
            ConsumeLogUiState(consumeLog = consumeLog)
        }
    }.cachedIn(viewModelScope)

}