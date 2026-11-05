package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfileData
import com.example.nutritiontracker.domain.model.UserProfileWithAllergens
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import com.example.nutritiontracker.domain.usecase.GetAllergensUseCase
import javax.inject.Inject

class GetUserProfileWithAllergensUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val getAllergensUseCase: GetAllergensUseCase
) {
    //TODO: INJECT GetAllergensUseCase later to merge it with UserProfile
    suspend operator fun invoke(): Result<UserProfileData<UserProfileWithAllergens>?> {
        val userProfileData = userProfileRepository.getUserProfile().getOrElse { err ->
            return Result.failure(err)
        }

        val allergens = getAllergensUseCase().getOrElse {
            return Result.failure(Exception("Failed To Get Allergies"))
        }

        val userWithAllergens = userProfileData?.data?.let { userProfile ->
            UserProfileWithAllergens(
                userProfile = userProfile,
                allergens = userProfile.allergenIds.map { userAllergies ->
                    allergens.first { it.id == userAllergies }
                }
            )
        }
        return Result.success(UserProfileData(fromLocal = true, data = userWithAllergens))
    }
}