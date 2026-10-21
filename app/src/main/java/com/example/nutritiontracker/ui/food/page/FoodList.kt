package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.viewmodel.FoodListViewModel

@Composable
fun FoodListPage(modifier: Modifier = Modifier, viewModel: FoodListViewModel = hiltViewModel()) {
    val lazyFoodItems = viewModel.foodsFlow.collectAsLazyPagingItems()
    ListPagingSection(
        modifier = modifier
            .fillMaxSize(),
        pagingItems = lazyFoodItems,
        key = { food -> food.id },
        initialDataError = {
            Text("Initial Data Error")
        }) { food ->
        FoodCard(food, onAddClick = {})
    }
}
