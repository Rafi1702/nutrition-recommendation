package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle

@Preview(showBackground = true)
@Composable
private fun UserNeedsCardPreview() {
    NutritionTrackerTheme {
        UserNeedsCard()
    }
}

@Composable
internal fun UserNeedsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
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
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = colorScheme.secondary
                        )
                        Text(
                            text = "Energi Harian",
                            style = typography.titleMedium.copy(
                                color = colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    EnergyConsumeIndicator(
                        indicatorColor = colorScheme.secondary,
                        trackColor = colorScheme.secondaryContainer
                    )
                }
            },
            rightContent = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Chip(
                        radius = 12.dp,
                        color = colorScheme.primaryContainer,
                        contentColor = colorScheme.onPrimaryContainer
                    ) {
                        Text(
                            text = "72.5% Tercapai",
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            style = typography.labelSmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tercatat",
                                    style = typography.labelMedium.copy(
                                        color = colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(text = buildAnnotatedString {
                                    withStyle(
                                        style = typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = colorScheme.onSurface
                                    ) {
                                        append("1400")
                                    }

                                    withStyle(
                                        style = typography.labelSmall,
                                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    ) {
                                        append("/1400")
                                    }
                                })
                            }
                            LinearProgressIndicator(
                                progress = { 0.5f },
                                color = colorScheme.primary,
                                trackColor = colorScheme.primaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
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
internal fun EnergyConsumeIndicator(
    modifier: Modifier = Modifier,
    indicatorColor: Color = colorScheme.secondary,
    trackColor: Color = colorScheme.secondaryContainer
) {
    val strokeWidth = 8.dp

    SubcomposeLayout(modifier = modifier) { constraints ->
        val textPlaceable = subcompose("text") {
            Text(
                text = "550 ${stringResource(R.string.total_calories)}",
                style = typography.labelMedium.copy(
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )
        }.first().measure(constraints.copy(minWidth = 0, minHeight = 0))

        val paddingPx = (strokeWidth + 8.dp).roundToPx()
        val diameter = maxOf(textPlaceable.width, textPlaceable.height) + (paddingPx * 2)

        val indicatorPlaceable = subcompose("indicator") {
            CircularProgressIndicator(
                progress = { 0.7f },
                strokeWidth = strokeWidth,
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                color = indicatorColor,
                trackColor = trackColor
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
