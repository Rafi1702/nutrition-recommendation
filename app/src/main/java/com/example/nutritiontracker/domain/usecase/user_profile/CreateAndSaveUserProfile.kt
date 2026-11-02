package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfileWithAllergens

class CreateAndSaveUserProfileUseCase(
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) {
    suspend operator fun invoke(userProfileWithAllergens: UserProfileWithAllergens): Result<Unit> {
        val saveUserProfile = saveUserProfileUseCase(userProfileWithAllergens).getOrElse { err ->
            return Result.failure(err)
        }

        val createUserProfile = createUserProfileUseCase(userProfileWithAllergens.userProfile).getOrElse { err ->
            return Result.failure(err)
        }

        return Result.success(Unit)
    }
}