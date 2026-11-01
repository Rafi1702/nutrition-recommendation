package com.example.nutritiontracker.domain.repository

import com.example.nutritiontracker.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile?>
    suspend fun createUserProfile(userProfile: UserProfile): Result<Unit>
}