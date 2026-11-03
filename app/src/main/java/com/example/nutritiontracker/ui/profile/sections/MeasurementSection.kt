package com.example.nutritiontracker.ui.profile.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.BodyMassIndexStatus
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.textfield.NumberPickerField
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.profile.page.label
import com.example.nutritiontracker.ui.profile.page.statusColor
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


@Composable
fun MeasurementSection() {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_biometric),
        icon = Icons.Default.Info,
    ) {
        MeasurementContent()
    }
}

@Composable
fun MeasurementContent() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            NumberPickerField(
                modifier = Modifier.weight(1f),
                fieldName = UserProfileForm::height,
                label = stringResource(R.string.user_profile_height_label),
                unit = stringResource(R.string.user_profile_height_unit)
            )
            NumberPickerField(
                modifier = Modifier.weight(1f),
                fieldName = UserProfileForm::weight,
                label = stringResource(R.string.user_profile_weight_label),
                unit = stringResource(R.string.user_profile_weight_unit)
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Spacing.s),
            color = colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.m),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Speed,
                    contentDescription = "accelerate_icon",
                    tint = colorScheme.primary
                )
                Text(
                    stringResource(R.string.body_mass_index),
                    style = typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "21.5",
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                )
                Chip(
                    color = BodyMassIndexStatus.NORMAL.statusColor().first,
                    contentColor = BodyMassIndexStatus.NORMAL.statusColor().second,
                ) {
                    Text(
                        BodyMassIndexStatus.NORMAL.label(),
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
