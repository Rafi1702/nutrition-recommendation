package com.example.nutritiontracker.ui.profile.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun UserProfilePagePreview() {
    NutritionTrackerTheme {
        UserProfileContent(form = FormBuilder())
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
private fun DailyActivitiesContentPreview() {
    NutritionTrackerTheme {
        DailyEstimationSection()
    }
}


@Preview(name = "UserAllergiesPreferencesSectionPreview", showBackground = true)
@Composable
private fun UserAllergiesPreferenceSectionPreview() {
    NutritionTrackerTheme {
        Form<UserProfileForm> {
            UserAllergiesPreferenceSection()
        }
    }
}


