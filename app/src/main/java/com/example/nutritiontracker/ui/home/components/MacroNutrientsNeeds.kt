package com.example.nutritiontracker.ui.home.components

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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle

@Composable
internal fun MacroNutrientsNeed(label: String, current: Float, target: Float, unit: String) {
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
                    Icon(
                        Icons.Default.Fastfood,
                        "${label}-icon",
                        modifier = Modifier.size(16.dp)
                    )
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
