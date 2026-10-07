package com.example.nutritiontracker.ui.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


data class CardFormHeaderProps(
    val title: String? = null,
    val icon: ImageVector? = null
)

@Composable
private fun CardFormHeader(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null,
) {
    if (icon != null && title != null) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = colorScheme.primary
            )

            Text(
                title,
                style = typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
private fun CardFormSection(
    modifier: Modifier = Modifier,
    cardHeaderProps: CardFormHeaderProps = CardFormHeaderProps(),
    contentPadding: PaddingValues = PaddingValues(Spacing.none),
    content: @Composable (() -> Unit)? = null
) {
    val (title, icon) = cardHeaderProps
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Spacing.m),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.xxs)
    ) {
        Column(
            modifier = Modifier.padding(vertical = Spacing.m),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            CardFormHeader(
                title = title,
                icon = icon,
                modifier = Modifier.padding(contentPadding)
            )
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
    label: String? = null,
    content: @Composable (() -> Unit)
) {
    CardFormSection(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = Spacing.m),
        cardHeaderProps = CardFormHeaderProps(
            icon = icon,
            title = label,
        )
    ) {
        content()
    }
}
