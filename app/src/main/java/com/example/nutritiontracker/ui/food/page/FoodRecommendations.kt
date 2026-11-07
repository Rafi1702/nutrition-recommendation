package com.example.nutritiontracker.ui.food.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.domain.model.RecommendationFood
import com.example.nutritiontracker.ui.HeaderProps
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.food.viewmodel.FoodRecommendationViewModel
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.utils.composable.plus
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FoodRecommendationsPage(
    modifier: Modifier = Modifier,
    viewModel: FoodRecommendationViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "Food Recommendation",
                style = LocalTextStyle.current.copy(color = colorScheme.onSurface)
            )
        })
    }) { innerPadding ->
        FoodRecommendationsContent(
            foodRecommendation = viewModel.foodsRecommendation,
            paddingValues = innerPadding + PaddingValues(vertical = Spacing.m)
        )
    }
}


@Composable
internal fun FoodRecommendationsContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues.Zero,
    foodRecommendation: Flow<PagingData<RecommendationFood>>
) {
    val foodRecommendationLazyPagingItems = foodRecommendation.collectAsLazyPagingItems()

    ListPagingSection(
        pagingItems = foodRecommendationLazyPagingItems,
        paddingValues = paddingValues,
        initialDataError = { error ->
            error.message?.let { Text(it, textAlign = TextAlign.Center) }
        },
        emptyDataContent = {
            Text("Food Recommendation is Empty")
        },
        headerProps = HeaderProps(
            true,
            @Composable {
                Row(
                    modifier = Modifier
                        .background(color = colorScheme.surface)
                        .padding(Spacing.s),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                ) {
                    Icon(Icons.Default.Info, contentDescription = "food_recommendation_information")
                    Text("Based on XXXX food is containing xx% carbs, xx% proteins, xx% fats")
                }
            },
        ),
        key = { index ->
            val item = foodRecommendationLazyPagingItems[index]
            if (item != null && item.food != null) {
                "food_${item.food.id}_$index"
            } else {
                "placeholder_$index"
            }
        },
        itemContent = { item ->
            item.food?.let { food ->
                FoodCard(food = food)
            }
        }
    )
}