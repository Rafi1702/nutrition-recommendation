package com.example.nutritiontracker.ui.profile.sections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.domain.model.PersonalActivitiesMap.toPersonalActivities
import com.example.nutritiontracker.ui.components.ColumnOptionForm
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.profile.page.PersonalActivitiesUtility
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


@Composable
fun DailyActivitiesSection() {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_activities),
        icon = Icons.Default.Info,
        modifier = Modifier.fillMaxWidth()
    ) {
        DailyActivitiesContent()
    }
}

@Composable
fun DailyActivitiesContent() {
    ColumnOptionForm(
        fieldName = UserProfileForm::personalActivities,
        verticalArrangement = Arrangement.spacedBy(Spacing.s),
        items = PersonalActivities.entries.map { it.label }
    ) { activity, isSelected ->
        Surface(
            shape = RoundedCornerShape(Spacing.s),
            color = if (isSelected) colorScheme.primary else colorScheme.surfaceVariant,
            contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.m)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) colorScheme.primaryContainer.copy(alpha = 0.25f) else colorScheme.surface,
                    contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
                ) {
                    Icon(
                        modifier = Modifier.padding(Spacing.s),
                        imageVector = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().icon()
                        },
                        contentDescription = "${activity}_icon",
                        tint = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().label().first
                        },
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                        )
                    )
                    Text(
                        text = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().label().second
                        },
                        style = typography.bodySmall.copy(
                            color = if (isSelected) colorScheme.onPrimary.copy(alpha = 0.85f) else colorScheme.onSurfaceVariant
                        )
                    )
                }

                if (isSelected) {
                    Surface(
                        shape = CircleShape,
                        color = colorScheme.onPrimary,
                        contentColor = colorScheme.primary
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "selected",
                            modifier = Modifier
                                .padding(Spacing.xxs)
                                .size(Spacing.m),
                            tint = colorScheme.primary
                        )
                    }
                } else {
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            Spacing.xxs / 2,
                            colorScheme.outline.copy(alpha = 0.3f)
                        )
                    ) {
                        Box(modifier = Modifier.size(Spacing.m))
                    }
                }
            }
        }
    }
}
