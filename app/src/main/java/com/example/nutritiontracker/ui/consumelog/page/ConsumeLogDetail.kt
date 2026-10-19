package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.nutritiontracker.ui.ListPagingSection
import com.example.nutritiontracker.ui.consumelog.viewmodel.ConsumeLogDetailViewModel
import com.example.nutritiontracker.ui.food.components.FoodCard
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.utils.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumeLogDetail(
    viewModel: ConsumeLogDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val lazyConsumeLogs = viewModel.consumeLogsFlow.collectAsLazyPagingItems()
    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick =  onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            title = { Text("Consume Logs") }
        )
    }) { innerPadding ->
        ListPagingSection(
            modifier = Modifier
                .fillMaxSize(),
            paddingValues = innerPadding + PaddingValues(horizontal = Spacing.m),
            pagingItems = lazyConsumeLogs,
            key = { consumeLogStatus -> consumeLogStatus.consumeLog.id },
            initialDataError = {
                Text("Initial Data Error")
            }) { consumeLog ->
            FoodCard(food = consumeLog.consumeLog.food)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsumeLogDetailPreview() {
    NutritionTrackerTheme {
        ConsumeLogDetail()
    }
}




