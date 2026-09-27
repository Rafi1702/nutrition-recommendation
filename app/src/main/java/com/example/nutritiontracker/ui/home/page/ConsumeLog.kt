package com.example.nutritiontracker.ui.home.page


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.datasource.remote.MOCK_CONSUME_LOGS
import com.example.nutritiontracker.ui.components.DatePicker


@Preview(showBackground = true)
@Composable
internal fun ConsumeLogPage(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            DatePicker(contentPadding = PaddingValues(horizontal = 16.dp))
        }
        items(MOCK_CONSUME_LOGS){

        }
    }
}