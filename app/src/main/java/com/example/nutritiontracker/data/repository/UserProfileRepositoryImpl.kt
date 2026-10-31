package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.model.user.toCreateUserProfile
import com.example.nutritiontracker.data.model.user.toUserProfile
import com.example.nutritiontracker.data.remote.UserProfileDataSource
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(private val userProfileDataSource: UserProfileDataSource) :
    UserProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfile?> {
        val userProfileResponse = safeCall { userProfileDataSource.getUserProfile() }

        return userProfileResponse.fold(
            onSuccess = { userProfile ->

                Result.success(userProfile.data?.toUserProfile())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )

    }

    override suspend fun createUserProfile(userProfile: UserProfile): Result<Unit> {
        return safeCall { userProfileDataSource.createUserProfile(userProfile.toCreateUserProfile()) }.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { err ->
                Result.failure(err)
            }
        )
    }
}