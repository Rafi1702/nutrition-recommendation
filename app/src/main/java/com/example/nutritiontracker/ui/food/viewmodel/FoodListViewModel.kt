package com.example.nutritiontracker.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.nutritiontracker.domain.usecase.GetFoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FoodListViewModel @Inject constructor(private val getFoodsUseCase: GetFoodsUseCase) :
    ViewModel() {
    val foodsFlow = getFoodsUseCase().cachedIn(viewModelScope)
}