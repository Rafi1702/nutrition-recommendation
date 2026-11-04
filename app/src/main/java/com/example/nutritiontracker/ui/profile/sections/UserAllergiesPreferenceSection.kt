package com.example.nutritiontracker.ui.profile.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.ui.components.MultiSelectionOptions
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.profile.page.LocalUserProfileForm
import com.example.nutritiontracker.ui.profile.page.LocalUserProfileUiState
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.theme.Spacing


@Composable
fun UserAllergiesPreferenceSection() {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_allergies),
        icon = Icons.Default.Info
    ) {
        UserAllergiesPreferenceContent()
    }
}

@Composable
fun UserAllergiesPreferenceContent() {
    val allergensState = LocalUserProfileUiState.current.value.allergensState
    when {
        allergensState.isLoading -> {
            Box(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        allergensState.error != null -> {
            Box(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(allergensState.error)
            }
        }

        else -> {
            MultiSelectionOptions(
                fieldName = UserProfileForm::userAllergies,
                initialValue = LocalUserProfileForm.current.getField(UserProfileForm::userAllergies)?.state?.value
                    ?: emptyList(),
                items = allergensState.allergens.map { it.name },
                horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) { item, isSelected ->
                SelectionChip(selected = isSelected) { Text(item) }
            }
        }
    }
}
