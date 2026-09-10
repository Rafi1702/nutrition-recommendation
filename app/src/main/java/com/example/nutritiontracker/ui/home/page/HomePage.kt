package com.example.nutritiontracker.ui.home.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomePage() {
    LazyColumn {
        item {
            UserNeedSection()
        }
    }
}

@Composable
private fun UserNeedSection(){
    Card(
        modifier = Modifier,
    ) {
        Column{
            
        }
    }
}

@Composable
private fun MacroNutrientsNeed(label: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Icon(
                Icons.Default.FoodBank,
                contentDescription = label,
                modifier = Modifier.size(18.dp)
            )
        }
        LinearProgressIndicator(
            progress = { 10f },
            modifier = Modifier
                .width(64.dp)
                .height(8.dp),
        )
        Text(text = "100gr", style = MaterialTheme.typography.labelSmall)
    }
}