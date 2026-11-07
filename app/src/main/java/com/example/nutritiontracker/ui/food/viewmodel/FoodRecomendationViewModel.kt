package com.example.nutritiontracker.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nutritiontracker.domain.usecase.GetFoodRecommendationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodRecommendationViewModel @Inject constructor(private val getFoodRecommendationUseCase: GetFoodRecommendationUseCase) :
    ViewModel() {
    val foodsRecommendation = getFoodRecommendationUseCase().cachedIn(viewModelScope)
}