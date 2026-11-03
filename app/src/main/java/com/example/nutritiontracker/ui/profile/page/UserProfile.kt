package com.example.nutritiontracker.ui.profile.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nutritiontracker.R
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.profile.sections.DailyActivitiesSection
import com.example.nutritiontracker.ui.profile.sections.DailyEstimationSection
import com.example.nutritiontracker.ui.profile.sections.DemographicSection
import com.example.nutritiontracker.ui.profile.sections.MeasurementSection
import com.example.nutritiontracker.ui.profile.sections.UserAllergiesPreferenceSection
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileContract
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileViewModel
import com.example.nutritiontracker.ui.theme.LocalNavController
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.utils.MutationState


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfilePageRoute(viewModel: UserProfileViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val userMutationState = uiState.value.userProfileMutationState
    val mutationLoading = userMutationState is MutationState.Pending
    //if navigated from authentication or if user still doesn't have profile
    val navController = viewModel.userProfile?.let { LocalNavController.current }


    val userProfileContract = object : UserProfileContract {
        override fun createUserProfile() {
            viewModel.createUserProfile()
        }

        override fun saveUserProfile() {
            viewModel.saveUserProfile()
        }
    }


    CompositionLocalProvider(
        LocalUserProfileUiState provides uiState,
        LocalUserProfileForm provides viewModel.userProfileForm
    ) {
        if (viewModel.userProfile != null) {
            UserProfileContent(
                modifier = Modifier
                    .systemBarsPadding(),
                userProfileContract = userProfileContract
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text("User Profile")
                    })
                },
                bottomBar = {
                    BottomAppBar {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = viewModel.userProfileForm.isValid && !mutationLoading,
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                viewModel.createUserProfile()
                            }
                        ) {
                            if (mutationLoading) {
                                CircularProgressIndicator()
                            } else {
                                Text(stringResource(R.string.user_profile_submit))
                            }
                        }
                    }
                }
            ) { innerPadding ->
                UserProfileContent(
                    modifier = Modifier
                        .padding(innerPadding)
                        .systemBarsPadding(),
                    userProfileContract = userProfileContract
                )
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileContent(
    modifier: Modifier = Modifier,
    userProfileContract: UserProfileContract = object : UserProfileContract {
        override fun createUserProfile() {}
        override fun saveUserProfile() {}
    },
) {
    val scrollState = rememberScrollState()

    Form(
        formBuilder = LocalUserProfileForm.current,
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = Spacing.m),
        verticalSpacing = Spacing.m
    ) { isValid ->
        DemographicSection()
        MeasurementSection()
        DailyActivitiesSection()
        UserAllergiesPreferenceSection()
        DailyEstimationSection(userProfileContract)
    }
}
