package com.example.nutritiontracker.ui.foodlist.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.ui.foodlist.component.FoodList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun Debounce(query: String, delayInMillis: Long = 300L, callback: (String) -> Unit) {
    val currentCallback by rememberUpdatedState(callback)
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            delay(delayInMillis)
            currentCallback(query)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListPage(padding: PaddingValues) {
    var query by remember { mutableStateOf("") }

    val someProcess = rememberCoroutineScope()

    Debounce(query) { _ ->
        // TODO: Invoke ViewModel to fetch or filter foods based on query
        someProcess.launch{
            Log.d("FAAAHHH", "PROCESSED")
        }
    }
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchBar(value = query, onValueChange = {
            query = it
        })

        FoodList(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), foods = MOCK_FOODS
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    val colors1 = SearchBarDefaults.colors()
    SearchBar(
        shape = RoundedCornerShape(8.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = value,
                onQueryChange = onValueChange,
                onSearch = {},
                expanded = true,
                onExpandedChange = { },
                enabled = true,
                placeholder = { Text(stringResource(id = R.string.food_search_bar_placeholder)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = null,
                colors = colors1.inputFieldColors,
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = Modifier.fillMaxWidth(),
        colors = colors1,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,
    ) {}
}