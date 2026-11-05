package com.example.nutritiontracker.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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
class FoodRecommendationViewModel @Inject constructor(private val getFoodRecommendationUseCase: GetFoodRecommendationUseCase) :
    ViewModel() {

    private val _uiState =
        MutableStateFlow<QueryState<List<RecommendationFood>>>(QueryState.Initial())

    val foodsRecommendation = getFoodRecommendationUseCase().cachedIn(viewModelScope)

    val uiState = _uiState.asStateFlow()

    init {
        getFoodRecommendation()
    }

    private fun getFoodRecommendation() {
        viewModelScope.launch {
            _uiState.update { QueryState.Loading }

            delay(500)
        }
    }
}