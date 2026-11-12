package com.example.nutritiontracker.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.usecase.consumelog.GetPersonalConsumeLogListUseCase
import com.example.nutritiontracker.ui.utils.composable.QueryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val getPersonalConsumeLogListUseCase: GetPersonalConsumeLogListUseCase) :
    ViewModel() {
    private val personalConsumeLogState = MutableStateFlow(
        mapOf<MealType, QueryState<List<ConsumeLog>>>(
            MealType.BREAKFAST to QueryState.Initial(emptyList()),
            MealType.LUNCH to QueryState.Initial(emptyList()),
            MealType.DINNER to QueryState.Initial(emptyList())
        )
    )

    init{
        getPersonalConsumeLogList(MealType.BREAKFAST, "")
        getPersonalConsumeLogList(MealType.LUNCH, "")
        getPersonalConsumeLogList(MealType.DINNER, "")
    }
    fun getPersonalConsumeLogList(mealType: MealType, consumedAt: String) {
        viewModelScope.launch {
            getPersonalConsumeLogListUseCase(mealType = mealType, consumedAt = consumedAt).fold(
                onSuccess = { data ->
                    personalConsumeLogState.update { previous ->
                        buildMap {
                            putAll(previous)
                            put(mealType, QueryState.Success(data))
                        }
                    }
                },
                onFailure = { error ->
                    personalConsumeLogState.update { previous ->
                        buildMap {
                            putAll(previous)
                            put(mealType, QueryState.Error(error))
                        }
                    }
                }
            )
        }


    }
}