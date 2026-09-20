package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle


@Composable
internal fun UserNeedsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        RowEqualHeight(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            leftContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = colorScheme.secondary)
                        Text("Energi Harian", style = MaterialTheme.typography.titleMedium.copy(color = colorScheme.onBackground))
                    }
                    EnergyConsumeIndicator(indicatorColor = colorScheme.secondary)
                }
            },
            rightContent = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Chip(
                        label = "72.5% Tercapai",
                        modifier = Modifier.background(color = colorScheme.primaryContainer)
                    )
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Tercatat")
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        typography.labelLarge,
                                        color = LocalContentColor.current
                                    ) {
                                        append("1400")
                                    }

                                    withStyle(
                                        typography.labelSmall,
                                        color = LocalContentColor.current.copy(alpha = .5f)
                                    ) {
                                        append("/1400")
                                    }
                                })
                            }
                            LinearProgressIndicator(progress = { .5f }, color= colorScheme.primary)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun RowEqualHeight(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val leftPlaceable = subcompose("left", leftContent).first().measure(
            constraints.copy(minWidth = 0, minHeight = 0)
        )

        val targetHeight = leftPlaceable.height
        val remainingWidth = (constraints.maxWidth - leftPlaceable.width).coerceAtLeast(0)

        val rightPlaceable = subcompose("right", rightContent).first().measure(
            Constraints.fixed(width = remainingWidth, height = targetHeight)
        )

        layout(constraints.maxWidth, targetHeight) {
            leftPlaceable.placeRelative(0, 0)
            rightPlaceable.placeRelative(leftPlaceable.width, 0)
        }
    }
}

@Composable
internal fun EnergyConsumeIndicator(modifier: Modifier = Modifier, indicatorColor: Color = colorScheme.primary) {
    val strokeWidth = 10.dp

    SubcomposeLayout(modifier = modifier) { constraints ->
        val textPlaceable = subcompose("text") {
            Text(
                text = "550 ${stringResource(R.string.total_calories)}",
                maxLines = 1
            )
        }.first().measure(constraints.copy(minWidth = 0, minHeight = 0))

        val paddingPx = (strokeWidth + 8.dp).roundToPx()
        val diameter = maxOf(textPlaceable.width, textPlaceable.height) + (paddingPx * 2)

        val indicatorPlaceable = subcompose("indicator") {
            CircularProgressIndicator(
                progress = { 0.7f },
                strokeWidth = strokeWidth,
                strokeCap = StrokeCap.Butt,
                gapSize = 0.dp,
                color = indicatorColor
            )
        }.first().measure(Constraints.fixed(diameter, diameter))

        layout(diameter, diameter) {
            indicatorPlaceable.placeRelative(0, 0)
            textPlaceable.placeRelative(
                x = (diameter - textPlaceable.width) / 2,
                y = (diameter - textPlaceable.height) / 2
            )
        }
    }
}
