package com.example.nutritiontracker.ui.home.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle
import kotlin.math.roundToInt

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
                    val totalCalories = stringResource(R.string.total_calories)
                    Chip(radius = 16.dp, label = "Status")
                    Text(text = name, style = typography.titleMedium)
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = typography.labelMedium,
                            color = LocalContentColor.current
                        ) {
                            append("345 $totalCalories")
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
                    Text(stringResource(R.string.add_to_consume_log))
                }
            }
        }
    }
}
