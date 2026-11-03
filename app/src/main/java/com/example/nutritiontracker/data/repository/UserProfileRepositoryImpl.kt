package com.example.nutritiontracker.data.repository

import com.example.nutritiontracker.data.local.UserProfileLocalDataSource
import com.example.nutritiontracker.data.model.user.toCreateUserProfile
import com.example.nutritiontracker.data.model.user.toSaveUserProfileLocal
import com.example.nutritiontracker.data.model.user.toUserProfile
import com.example.nutritiontracker.data.remote.UserProfileDataSource
import com.example.nutritiontracker.data.utils.NetworkException
import com.example.nutritiontracker.data.utils.safeCall
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.UserProfileData
import com.example.nutritiontracker.domain.model.UserProfileWithAllergens
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileDataSource: UserProfileDataSource,
    private val userProfileLocalDataSource: UserProfileLocalDataSource
) :
    UserProfileRepository {
    override suspend fun getUserProfile(): Result<UserProfileData<UserProfile>?> {

        val userProfileResponse =
            safeCall { userProfileDataSource.getUserProfile() }.getOrElse { err ->
                if (err is NetworkException.NotFound) {
                    return@getOrElse null
                } else {
                    return Result.failure(err)
                }
            }?.data

        if (userProfileResponse != null) {
            return Result.success(UserProfileData(data = userProfileResponse.toUserProfile()))
        }

        val localUserProfile = userProfileLocalDataSource.getTemporaryUserProfile().first()

        return localUserProfile.fold(
            onSuccess = { userProfile ->
                Result.success(
                    UserProfileData(
                        fromLocal = true,
                        data = userProfile?.toUserProfile()
                    )
                )
            },
            onFailure = { error ->
                Result.success(null)
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

    override suspend fun saveUserProfile(userProfileWithAllergens: UserProfileWithAllergens): Result<Unit> {
        return userProfileLocalDataSource.setTemporaryUserProfile(userProfileWithAllergens.toSaveUserProfileLocal())
            .fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { err -> Result.failure(err) }
            )
    }
}