package com.example.nutritiontracker.ui.consumelog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.usecase.GetConsumeLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject


data class ConsumeLogUiStatus(
    val consumeLog: ConsumeLog,
    val isLoading: Boolean = false,
)

@HiltViewModel
class ConsumeLogDetailViewModel @Inject constructor(
    getConsumeLogUseCase: GetConsumeLogUseCase,
) : ViewModel() {

    val consumeLogsFlow = getConsumeLogUseCase().map { consumeLogs ->
        consumeLogs.map { consumeLog ->
            ConsumeLogUiStatus(consumeLog = consumeLog)
        }
    }.cachedIn(viewModelScope)

}