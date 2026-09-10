package com.example.nutritiontracker.ui.foodlist.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.nutritiontracker.domain.Food

@Composable
fun FoodCard(food: Food) {
    val (protein, carbs, fat) = food.macros
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Thumbnail(name = food.name)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = food.name, style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroNutrients(protein.type.name)
                    MacroNutrients(carbs.type.name)
                    MacroNutrients(fat.type.name)
                }
            }
        }
    }
}

@Composable
private fun Thumbnail(imageUrl: String? = null, name: String) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = name,
        modifier = Modifier
            .size(80.dp),
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        },
        error = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error")
            }
        }
    )
}

@Composable
private fun MacroNutrients(label: String) {
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
        Text(text = "100gr", style = MaterialTheme.typography.labelSmall)
    }
}


/*
LinearProgressIndicator(
            progress = { 10f },
            modifier = Modifier
                .width(64.dp)
                .height(8.dp),
        )
*/