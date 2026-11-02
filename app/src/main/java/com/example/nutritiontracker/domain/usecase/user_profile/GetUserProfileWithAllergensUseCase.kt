package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfileWithAllergens
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import com.example.nutritiontracker.domain.usecase.GetAllergensUseCase
import javax.inject.Inject

class GetUserProfileWithAllergensUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val getAllergensUseCase: GetAllergensUseCase
) {
    //TODO: INJECT GetAllergensUseCase later to merge it with UserProfile
    suspend operator fun invoke(): Result<UserProfileWithAllergens?> {
        val allergens = getAllergensUseCase().getOrElse {
            return Result.failure(Exception("Failed To Get Allergies"))
        }

        val userProfile = userProfileRepository.getUserProfile().getOrElse {
            return Result.failure(Exception("Failed to Get UserProfile"))
        }

        if (userProfile == null) {
            return Result.success(userProfile)
        }

        val userWithAllergens = UserProfileWithAllergens(
            userProfile = userProfile,
            allergens = userProfile.allergenIds.map { userAllergies ->
                allergens.filter { it.id == userAllergies }.first()
            }
        )
        return Result.success(userWithAllergens)
    }
}