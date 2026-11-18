package com.example.nutritiontracker.ui.food.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.nutritiontracker.domain.model.Food
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.domain.usecase.GetFoodsUseCase
import com.example.nutritiontracker.domain.usecase.consumelog.AddFoodsToConsumeLogUseCase
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel
import com.example.nutritiontracker.ui.utils.composable.MutationState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel(assistedFactory = FoodListViewModel.Factory::class)
class FoodListViewModel @AssistedInject constructor(
    getFoodsUseCase: GetFoodsUseCase,
    private val addFoodsToConsumeLogUseCase: AddFoodsToConsumeLogUseCase,
    @Assisted private val mealType: MealType,
    @Assisted private val consumedAt: String,
) :
    ViewModel() {
    private val foodMutations = MutableStateFlow(emptyMap<Int, FoodUiState>())
    private val _uiState = MutableStateFlow<MutationState<Unit>>(MutationState.Idle)

    private val _availableFoods = mutableListOf<Food>()
    val availableFoods: List<Food>
        get() = _availableFoods.toList()

    val uiState = _uiState.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(
            mealType: MealType = MealType.UNSPECIFIED,
            consumedAt: String = ""
        ): FoodListViewModel
    }

    val foodsFlow =
        getFoodsUseCase().map { data ->
            data.map { food ->
                _availableFoods += food
                FoodUiState(
                    food = food,
                    isLoading = false
                )
            }
        }.cachedIn(viewModelScope).combine(foodMutations) { pagingData, mutations ->
            pagingData.map { uiState ->
                val mutationOverride = mutations[uiState.food.id]
                mutationOverride ?: uiState
            }
        }

    fun addFoodToConsumeLog(food: Food, mealType: MealType? = null) {
        val currentMealType = mealType ?: this.mealType
        val loading = foodMutations.value[food.id]?.isLoading == true

        if (currentMealType == MealType.UNSPECIFIED && loading) return

        viewModelScope.launch {
            foodMutations.update { currentMap ->
                currentMap + (food.id to FoodUiState(food = food, isLoading = true))
            }

            delay(3000)

            addFoodsToConsumeLogUseCase(listOf(food.id), currentMealType, consumedAt).fold(
                onSuccess = {

                },
                onFailure = { err ->

                }
            )

            foodMutations.update { currentMap ->
                currentMap - food.id
            }
        }
    }

    fun addAllFoodsToConsumeLog(consumeMeals: Map<MealType, List<Int>>) {
        viewModelScope.launch {
            Log.d("[FOOD_LIST_VIEWMODEL]", "params: $consumeMeals")
            _uiState.update { MutationState.Pending }
            delay(1000)

            addFoodsToConsumeLogUseCase(consumeMeals, consumedAt).fold(
                onSuccess = { data ->
                    _uiState.update { MutationState.Success(data) }
                },
                onFailure = { err ->
                    _uiState.update { MutationState.Error(err) }
                }
            )

            delay(500)
            _uiState.update { MutationState.Idle }
        }
    }
}