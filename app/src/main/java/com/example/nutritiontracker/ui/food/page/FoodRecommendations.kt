package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.ui.HeaderProps
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.viewmodel.FoodRecommendationViewModel
import com.example.nutritiontracker.ui.theme.colorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FoodRecommendationsPage(
    modifier: Modifier = Modifier,
    viewModel: FoodRecommendationViewModel = hiltViewModel()
) {
    val foodRecommendationFlow = viewModel.foodsRecommendation.collectAsLazyPagingItems()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "Food Recommendation",
                style = LocalTextStyle.current.copy(color = colorScheme.onSurface)
            )
        })
    }) { innerPadding ->
        FoodRecommendationsContent(
            modifier = Modifier.padding(innerPadding),
            foodRecommendation = foodRecommendationFlow
        )
    }
}


@Composable
internal fun FoodRecommendationsContent(
    modifier: Modifier = Modifier,
    foodRecommendation: LazyPagingItems<RecommendationFood>
) {
    ListPagingSection(
        pagingItems = foodRecommendation,
        headerProps = HeaderProps(
            true,
            @Composable {
                Row(
                    modifier = Modifier
                        .background(color = colorScheme.surface)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Default.Info, contentDescription = "food_recommendation_information")
                    Text("Based on XXXX food is containing xx% carbs, xx% proteins, xx% fats")
                }
            },
        ),
        key = { it.food.id }
    ) {
        FoodCard(food = it.food)
    }
}
