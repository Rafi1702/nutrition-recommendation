package com.example.nutritiontracker.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.domain.usecase.GetFoodRecommendationUseCase
import com.example.nutritiontracker.ui.utils.QueryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodRecomendationViewModel @Inject constructor(private val getFoodRecommendationUseCase: GetFoodRecommendationUseCase) :
    ViewModel() {

    private val _uiState =
        MutableStateFlow<QueryState<List<RecommendationFood>>>(QueryState.Initial())

    val uiState = _uiState.asStateFlow()

    init {
        getFoodRecommendation()
    }

    private fun getFoodRecommendation() {
        viewModelScope.launch {
            _uiState.update { QueryState.Loading }

            delay(500)

            getFoodRecommendationUseCase().fold(
                onSuccess = { data ->
                    _uiState.update { QueryState.Success(data) }
                },
                onFailure = { error ->
                    _uiState.update { QueryState.Error(error) }
                }
            )
        }
    }
}