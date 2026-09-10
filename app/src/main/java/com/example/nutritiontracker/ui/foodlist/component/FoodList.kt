package com.example.nutritiontracker.ui.foodlist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.domain.Food


@Composable
fun FoodList(modifier: Modifier = Modifier, foods: List<Food> = emptyList()) {

    if (foods.isEmpty()) {
        return Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Food is Empty")
        }
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(foods) { food ->
            FoodCard(food = food)
        }
    }
}
