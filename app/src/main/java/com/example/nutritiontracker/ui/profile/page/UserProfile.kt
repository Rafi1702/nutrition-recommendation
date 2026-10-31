package com.example.nutritiontracker.ui.profile.page

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.BodyMassIndexStatus
import com.example.nutritiontracker.domain.model.Gender
import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.domain.model.PersonalActivitiesMap.toPersonalActivities
import com.example.nutritiontracker.domain.model.createDefaultUserProfile
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.ColumnOptionForm
import com.example.nutritiontracker.ui.components.DatePicker
import com.example.nutritiontracker.ui.components.DatePickerField
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.MultiSelectionOptions
import com.example.nutritiontracker.ui.components.RowOptionForm
import com.example.nutritiontracker.ui.components.SelectionChip
import com.example.nutritiontracker.ui.components.textfield.NumberPickerField
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileUiState
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel
import com.example.nutritiontracker.ui.theme.LocalUserProfile
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle


private val LocalUserProfileUiState =
    compositionLocalOf<State<UserProfileUiState>> { error("No ui state provided") }

private val LocalUserProfileForm =
    compositionLocalOf<FormBuilder<UserProfileForm>> { error("No user profile form provided") }

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfilePageRoute(viewModel: UserProfileViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("User Profile")
            })
        }
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalUserProfileUiState provides uiState,
            LocalUserProfileForm provides viewModel.userProfileForm
        ) {
            UserProfileContent(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(innerPadding),
                viewModel.userProfileForm
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileContent(modifier: Modifier = Modifier, form: FormBuilder<UserProfileForm>) {
    val scrollState = rememberScrollState()
    Form(
        formBuilder = form,
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = Spacing.m),
        verticalSpacing = Spacing.m
    ) { isValid ->
        DemographicSection()
        MeasurementSection()
        DailyActivitiesSection()
        DailyEstimationSection()
        UserAllergiesPreferenceSection()
        Button(enabled = isValid, onClick = {}) {
            Text(stringResource(R.string.user_profile_submit))
        }
    }
}


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
            items = Gender.entries.map { it.label() },
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
                    gender.toGender().icon()?.let { icon ->
                        Icon(
                            icon,
                            contentDescription = "option_${gender.toGender().label()}",
                            tint = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                        )
                    }
                    Text(
                        gender.toGender().label(),
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

@Composable
private fun MeasurementSection() {
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


@Composable
private fun DailyActivitiesSection() {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_activities),
        icon = Icons.Default.Info,
        modifier = Modifier.fillMaxWidth()
    ) {
        DailyActivitiesContent()
    }
}

@Composable
private fun DailyActivitiesContent() {
    ColumnOptionForm(
        fieldName = UserProfileForm::personalActivities,
        verticalArrangement = Arrangement.spacedBy(Spacing.s),
        items = PersonalActivities.entries.map { it.name }
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

@Composable
fun UserAllergiesPreferenceSection() {
    UserProfileContentSection(label = "User Allergies", icon = Icons.Default.Info) {
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


@Composable
fun DailyEstimationSection() {
    UserProfileContentSection(
        label = stringResource(R.string.user_profile_section_diet),
        icon = Icons.Default.Info,
    ) {
        DailyEstimationContent()
    }
}

@Composable
private fun DailyEstimationContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        DailyEstimationContentHeader()
        DailyEstimationContentBody()
    }
}

@Composable
private fun DailyEstimationContentHeader() {
    val kcalPerDay = stringResource(R.string.user_profile_kcal_per_day)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically
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
                    append("~${createDefaultUserProfile().nutritionNeeds}\t")
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
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Chip(
                color = colorScheme.primaryContainer,
                contentColor = colorScheme.onPrimaryContainer
            ) {
                Text(
                    stringResource(R.string.user_profile_tdee_ready),
                    style = typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Text(
                stringResource(R.string.user_profile_deficit_sample),
                style = typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun DailyEstimationContentBody() {
    Button(
        shape = RoundedCornerShape(Spacing.s),
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        )
    ) {
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


