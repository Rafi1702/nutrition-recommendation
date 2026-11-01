package com.example.nutritiontracker.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nutritiontracker.domain.usecase.GetFoodRecommendationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodRecomendationViewModel @Inject constructor(private val getFoodRecommendationUseCase: GetFoodRecommendationUseCase) :
    ViewModel() {

}