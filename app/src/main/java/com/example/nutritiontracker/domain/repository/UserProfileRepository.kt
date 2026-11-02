package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.UserProfileWithAllergens

interface UserProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile?>
    suspend fun createUserProfile(userProfile: UserProfile): Result<Unit>
    suspend fun saveUserProfile(userProfileWithAllergens: UserProfileWithAllergens) : Result<Unit>
}