package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfileWithAllergens
import javax.inject.Inject

class CreateAndSaveUserProfileUseCase @Inject constructor(
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) {
    suspend operator fun invoke(userProfileWithAllergens: UserProfileWithAllergens): Result<Unit> {
        saveUserProfileUseCase(userProfileWithAllergens).getOrElse { err ->
            return Result.failure(err)
        }

        createUserProfileUseCase(userProfileWithAllergens.userProfile).getOrElse { err ->
            return Result.failure(err)
        }

        return Result.success(Unit)
    }
}
