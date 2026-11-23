package com.example.nutritiontracker.ui.consumelog.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.example.nutritiontracker.data.mocks.MOCK_CONSUME_LOGS
import com.example.nutritiontracker.data.mocks.MOCK_FOODS
import com.example.nutritiontracker.domain.model.ConsumeLog
import com.example.nutritiontracker.domain.model.MealType
import com.example.nutritiontracker.ui.consumelog.model.ConsumeLogUiState
import com.example.nutritiontracker.ui.food.model.FoodUiState
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Preview(showBackground = true)
@Composable
private fun PersonalConsumeListPreview() {
    val pagingData = PagingData.from(MOCK_CONSUME_LOGS.map { item ->
        ConsumeLogUiState(
            consumeLog = item,
            isLoading = false
        )
    })
    val fakeDataFlow = MutableStateFlow(pagingData)
    NutritionTrackerTheme {
        PersonalConsumeList(
            innerPadding = PaddingValues(horizontal = 16.dp),
            consumeLogs = fakeDataFlow
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConsumeLogDetailHeaderPreview(){
    NutritionTrackerTheme {
        ConsumeLogHeader()
    }
}



@Preview(showBackground = true)
@Composable
private fun HeaderConsumePreview(){

}
