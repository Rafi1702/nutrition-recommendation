package com.example.nutritiontracker.ui.home.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.nutritiontracker.datasource.remote.MOCK_FOODS
import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.ui.home.components.MacroNutrientsNeed
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.Debounce
import com.example.nutritiontracker.ui.utils.withStyle
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@Composable
fun HomePage(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            UserNeedSection()
        }
    }
}


private suspend fun getFoods(): List<Food> {
    delay(300)
    return MOCK_FOODS
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFF808080
)


@Composable
private fun RecommendationFoodSection() {
    var recommendationFoods by remember { mutableStateOf(MOCK_FOODS) }
    Debounce("User Needs") {
        getFoods()
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(recommendationFoods) { food ->
            FoodCard(food = food)
        }
    }
}

@Composable
internal fun FoodCard(food: Food) {
    val (_, name, macros) = food
    Card(modifier = Modifier.clip(RoundedCornerShape(8.dp))) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SubcomposeAsyncImage(
                    model = name,
                    contentDescription = name,
                    modifier = Modifier
                        .size(96.dp),
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
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = colorScheme.onTertiary,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error")
                        }
                    }
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Chip(radius = 16.dp, label = "Status")
                    Text(text = "Nom NOm Nom Nom Nom Nom Nom Nom", style = typography.titleMedium)
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = typography.labelMedium,
                            color = LocalContentColor.current
                        ) {
                            append("345 kkal")
                        }
                    })

                    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        macros.forEach {
                            val (type, serve) = it
                            Chip(label = "${serve.roundToInt()}g ${stringResource(type.label)}")
                        }
                    }
                }
            }
            FilledTonalButton(onClick = {}) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.AddCircleOutline, contentDescription = "add")
                    Text("Tambah ke catatan")
                }
            }
        }
    }
}

@Composable
internal fun Chip(radius: Dp? = null, content: @Composable () -> Unit) {
    Surface(
        shape = if (radius != null) RoundedCornerShape(radius) else AssistChipDefaults.shape,
        color = colorScheme.primary,
        contentColor = colorScheme.onPrimary,
        content = content
    )
}

@Composable
internal fun Chip(radius: Dp? = null, label: String) {
    Chip(radius) {
        Text(label, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp), style = typography.labelSmall)
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF808080
)
@Composable
private fun UserNeedSection(modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Makro Nutrisi Hari Ini", style = typography.labelLarge)
            Text(
                "Target gram",
                style = typography.labelSmall.copy(color = LocalContentColor.current.copy(alpha = .5f))
            )
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MacroNutrientsNeed(
                label = "PROTEIN",
                current = 90f,
                target = 130f,
                unit = "g"
            )
            MacroNutrientsNeed(
                label = "PROTEIN",
                current = 90f,
                target = 130f,
                unit = "g"
            )
            MacroNutrientsNeed(
                label = "PROTEIN",
                current = 90f,
                target = 130f,
                unit = "g"
            )
        }
    }
}