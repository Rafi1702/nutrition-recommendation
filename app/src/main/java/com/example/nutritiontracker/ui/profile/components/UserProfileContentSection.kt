package com.example.nutritiontracker.ui.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.profile.page.DemographicContent
import com.example.nutritiontracker.ui.theme.typography


data class CardFormHeaderProps(
    val title: String? = null,
    val icon: ImageVector? = null
)

@Composable
private fun CardFormHeader(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(icon, contentDescription = null)
        }
        Text(title, style = typography.titleMedium)
    }
}

@Composable
private fun CardFormSection(
    modifier: Modifier = Modifier,
    cardHeaderProps: CardFormHeaderProps = CardFormHeaderProps(),
    contentPadding: PaddingValues = PaddingValues.Zero,
    content: @Composable (() -> Unit)? = null
) {
    val (title, icon) = cardHeaderProps
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            title?.let {
                CardFormHeader(
                    title = title,
                    icon = icon,
                    modifier = Modifier.padding(contentPadding)
                )
            }
            Box(modifier = Modifier.padding(contentPadding)) {
                content?.invoke()
            }
        }
    }
}

@Composable
internal fun UserProfileContentSection(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    label: String,
    content: @Composable (() -> Unit)
) {
    CardFormSection(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        cardHeaderProps = CardFormHeaderProps(
            icon = icon,
            title = label,
        )
    ) {
        content()
    }
}