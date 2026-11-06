package com.example.nutritiontracker.ui.profile.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.createDefaultUserProfile
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.profile.page.LocalCurrentUserData
import com.example.nutritiontracker.ui.profile.page.LocalUserProfileForm
import com.example.nutritiontracker.ui.profile.page.LocalUserProfileUiState
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileContract
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.theme.userProfile
import com.example.nutritiontracker.ui.utils.composable.MutationState
import com.example.nutritiontracker.ui.utils.composable.withStyle


@Composable
fun DailyEstimationSection(
    userProfileContract: UserProfileContract = object : UserProfileContract {
        override fun createUserProfile() {}
        override fun saveUserProfile() {}
    },
) {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_diet),
        icon = Icons.Default.Info,
    ) {
        DailyEstimationContent(
            userProfileContract::createUserProfile,
            userProfileContract::saveUserProfile
        )
    }
}

@Composable
private fun DailyEstimationContent(
    onCreateUserProfile: () -> Unit = {},
    onSaveUserProfile: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        DailyEstimationContentHeader()
        DailyEstimationContentBody(onCreateUserProfile, onSaveUserProfile)
    }
}

@Composable
private fun DailyEstimationContentHeader() {
    val kcalPerDay = stringResource(R.string.user_profile_kcal_per_day)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
    ) {
        Surface(
            shape = CircleShape,
            color = colorScheme.secondaryContainer
        ) {
            Icon(
                Icons.Default.LocalFireDepartment,
                modifier = Modifier.padding(Spacing.s),
                contentDescription = "daily_estimation",
                tint = colorScheme.secondary
            )
        }
        Column {
            Text(
                stringResource(R.string.user_profile_daily_estimation_section_title),
                style = typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.primary
                ) {
                    append("~${createDefaultUserProfile()?.nutritionNeeds ?: 0}\t")
                }
                withStyle(
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                ) {
                    append(kcalPerDay)
                }
            })
        }
        Spacer(modifier = Modifier.weight(1f))

        val chipProps = if (userProfile != null) Triple(
            colorScheme.primaryContainer,
            colorScheme.onPrimaryContainer,
            stringResource(R.string.user_profile_tdee_ready)
        ) else Triple(
            colorScheme.surfaceVariant,
            colorScheme.onSurfaceVariant,
            stringResource(R.string.user_profile_tdee_not_ready)
        )

        val (containerColor, contentColor, label) = chipProps

        Chip(
            color = containerColor,
            contentColor = contentColor
        ) {
            Text(
                label,
                style = typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun DailyEstimationContentBody(
    onCreateUserProfile: () -> Unit = {},
    onSaveUserProfile: () -> Unit = {}
) {
    val userMutationState = LocalUserProfileUiState.current.value.userProfileMutationState
    val mutationLoading = userMutationState is MutationState.Pending
    val currentUser = LocalCurrentUserData.current
    val isFormValid = LocalUserProfileForm.current.isValid

    currentUser?.let{
        if (!currentUser.fromLocal) {
            Button(
                shape = RoundedCornerShape(Spacing.s),
                onClick = onCreateUserProfile,
                modifier = Modifier.fillMaxWidth(),
                enabled = !mutationLoading && isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary
                )
            ) {
                if (mutationLoading) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        Icons.Default.Calculate,
                        contentDescription = "calculate_nutrients_need",
                        modifier = Modifier.padding(end = Spacing.xs)
                    )
                    Text(
                        stringResource(R.string.user_profile_calculate_nutrition_button),
                        style = typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s)) {
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = onSaveUserProfile,
                    enabled = !mutationLoading && isFormValid,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    if (mutationLoading) {
                        CircularProgressIndicator()
                    } else {
                        Icon(Icons.Default.Save, contentDescription = "icon_save_profile")
                        Spacer(modifier = Modifier.width(Spacing.s))
                        Text(stringResource(R.string.save_label))
                    }
                }

                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    onClick = onCreateUserProfile,
                    enabled = !mutationLoading  && isFormValid,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    if (mutationLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            "${stringResource(R.string.save_label)} & ${stringResource(R.string.continue_label)}",
                            style = typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
