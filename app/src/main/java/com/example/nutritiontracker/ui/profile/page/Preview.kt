package com.example.nutritiontracker.ui.profile.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.domain.model.Allergens
import com.example.nutritiontracker.domain.model.createDefaultUserProfile
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.profile.sections.DailyEstimationSection
import com.example.nutritiontracker.ui.profile.sections.MeasurementContent
import com.example.nutritiontracker.ui.profile.sections.UserAllergiesPreferenceSection
import com.example.nutritiontracker.ui.profile.viewmodel.AllergensState
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileUiState
import com.example.nutritiontracker.ui.theme.LocalUserProfile
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun UserProfilePagePreview() {
    val uiState = remember {
        mutableStateOf(UserProfileUiState())
    }

    CompositionLocalProvider(
        LocalUserProfileUiState provides uiState,
        LocalUserProfileForm provides FormBuilder()
    ) {
        NutritionTrackerTheme {
            UserProfileContent()
        }
    }
}

@Preview
@Composable
private fun MeasurementContentPreview() {
    NutritionTrackerTheme {
        Form<UserProfileForm> {
            MeasurementContent()
        }
    }
}

@Preview
@Composable
private fun DailyActivitiesContentNoUserPreview() {
    val uiState = remember {
        mutableStateOf(UserProfileUiState())
    }
    CompositionLocalProvider(LocalUserProfileUiState provides uiState) {
        NutritionTrackerTheme {
            DailyEstimationSection()
        }
    }
}


@Preview
@Composable
private fun DailyActivitiesContentWithUserPreview() {
    val profile = remember { mutableStateOf(createDefaultUserProfile()) }
    val uiState = remember {
        mutableStateOf(UserProfileUiState())
    }
    CompositionLocalProvider(
        LocalUserProfileUiState provides uiState,
        LocalUserProfile provides profile
    ) {
        NutritionTrackerTheme {
            DailyEstimationSection()
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun UserAllergiesPreferenceSectionPreview() {
    val uiState = remember {
        mutableStateOf(
            UserProfileUiState(
                allergensState = AllergensState(
                    allergens = listOf(
                        Allergens(id = 1, name = "Peanuts")
                    )
                )
            )
        )
    }
    val userProfileForm = remember { FormBuilder<UserProfileForm>() }
    CompositionLocalProvider(
        LocalUserProfileUiState provides uiState,
        LocalUserProfileForm provides userProfileForm
    ) {
        NutritionTrackerTheme {
            Form(formBuilder = userProfileForm) {
                UserAllergiesPreferenceSection()
            }
        }
    }
}
