package com.example.nutritiontracker.ui.profile.page

import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.UserProfileData
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileForm
import com.example.nutritiontracker.ui.profile.viewmodel.UserProfileUiState


internal val LocalUserProfileUiState =
    compositionLocalOf<State<UserProfileUiState>> { error("No ui state provided") }

internal val LocalUserProfileForm =
    compositionLocalOf<FormBuilder<UserProfileForm>> { error("No user profile form provided") }

internal val LocalCurrentUserData = compositionLocalOf<UserProfileData<UserProfile>?>{ error("No user data provided") }

