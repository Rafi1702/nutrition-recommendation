package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel
import com.example.nutritiontracker.ui.theme.Spacing

@Composable
fun FoodListPage(viewModel: FoodListViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            uiState.value.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.value.errorMessage != null ->{
                Text("${uiState.value.errorMessage}")
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
                    items(uiState.value.foods, key = { it.id }) { food ->
                        FoodCard(food = food)
                    }
                }
            }
        }
    }
}