package com.example.nutritiontracker.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.usecase.GetFoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class FoodUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val foods: List<Food> = emptyList()
)

@HiltViewModel
class FoodListViewModel @Inject constructor(private val getFoodsUseCase: GetFoodsUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(FoodUiState())

    val foodsFlow = getFoodsUseCase().cachedIn(viewModelScope)
    val uiState = _uiState.asStateFlow()

//    init{
//        getFoods()
//    }
//
//    fun getFoods() {
//        _uiState.update { it.copy(isLoading = true) }
//        viewModelScope.launch {
//            getFoodsUseCase().fold(
//                onSuccess = { foods ->
//                    _uiState.update { it.copy(isLoading = false, foods = foods) }
//                },
//                onFailure = { error ->
//                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
//                }
//            )
//        }
//    }
}