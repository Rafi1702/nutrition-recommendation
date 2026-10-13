package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.user.toUserProfile
import com.example.nutritiontracker.data.remote.UserProfileDataSource
import com.example.nutritiontracker.data.safeCall
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(private val userProfileDataSource: UserProfileDataSource) :
    UserProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfile?> {
        val userProfileResponse = safeCall { userProfileDataSource.getUserProfile() }


        return userProfileResponse.fold(
            onSuccess = { userProfile ->

                Result.success(userProfile?.toUserProfile())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )

    }
}