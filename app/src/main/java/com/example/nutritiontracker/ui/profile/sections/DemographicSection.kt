package com.example.nutritiontracker.ui.profile.sections

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.Gender
import com.example.nutritiontracker.domain.model.GenderMap.toGender
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.DatePickerField
import com.example.nutritiontracker.ui.components.RowOptionForm
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.profile.page.GenderUtility
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DemographicSection() {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_demographic),
        icon = Icons.Default.Info,
    ) {
        DemographicContent()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DemographicContent() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
        Text(
            stringResource(R.string.user_profile_biological_gender),
            style = typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        )
        RowOptionForm(
            fieldName = UserProfileForm::gender,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            items = Gender.entries.map { it.label },
            itemWeight = 1f
        ) { gender, isSelected ->
            Surface(
                color = if (isSelected) colorScheme.primary else colorScheme.surfaceVariant,
                contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface,
                shape = RoundedCornerShape(Spacing.s)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.m, horizontal = Spacing.s),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    val genderEnum = gender.toGender()
                    Icon(
                        imageVector = with(GenderUtility) { genderEnum.icon() },
                        contentDescription = "option_${with(GenderUtility) { genderEnum.label() }}",
                        tint = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                    )
                    Text(
                        text = with(GenderUtility) { genderEnum.label() },
                        style = typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.user_profile_birth_date),
                    style = typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                )
                Chip(
                    color = colorScheme.primaryContainer,
                    contentColor = colorScheme.onPrimaryContainer
                ) {
                    Text(
                        stringResource(R.string.user_profile_sample_age),
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            DatePickerField(
                UserProfileForm::birth,
            )
        }
    }
}
