package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.UserProfileData
import com.example.nutritiontracker.domain.model.UserProfileWithAllergens

interface UserProfileRepository {
    suspend fun getUserProfile(): Result<UserProfileData<UserProfile>?>
    suspend fun createUserProfile(userProfile: UserProfile): Result<Unit>
    suspend fun saveUserProfile(userProfileWithAllergens: UserProfileWithAllergens): Result<Unit>
}