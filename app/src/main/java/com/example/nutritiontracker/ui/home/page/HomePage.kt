package com.example.nutritiontracker.ui.home.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.domain.MacroType
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


@Composable
fun HomePage(modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            UserNeedSection()
        }
    }
}

@Preview
@Composable
private fun UserNeedSection(modifier: Modifier = Modifier) {
    Column{
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
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



@Composable
private fun MacroNutrientsNeed(label: String, current: Float, target: Float, unit: String) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .background(colorScheme.primary)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(label)
                    Icon(Icons.Default.Fastfood, "${label}-icon", modifier = Modifier.size(16.dp))
                }
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = typography.headlineSmall,
                        color = colorScheme.onSurface
                    ) {
                        append(current.toInt().toString())
                    }

                    withStyle(
                        style = typography.labelSmall,
                        color = colorScheme.onSurface.copy(.5f)
                    ) {
                        append("/${target.toInt()}$unit")
                    }
                })
                Text("Sisa ${(target - current).toInt()}$unit")
                LinearProgressIndicator(
                    progress = { current / target },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
        }
    }
}


fun AnnotatedString.Builder.withStyle(
    style: TextStyle,
    color: Color,
    block: AnnotatedString.Builder.() -> Unit,
) {
    withStyle(
        style = SpanStyle(
            color = color,
            fontSize = style.fontSize,
            fontWeight = style.fontWeight,
            fontStyle = style.fontStyle,
            fontFamily = style.fontFamily,
            letterSpacing = style.letterSpacing,
        ), block = block
    )
}