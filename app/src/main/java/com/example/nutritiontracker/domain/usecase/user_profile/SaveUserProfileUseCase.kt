package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfileWithAllergens
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(private val userProfileRepository: UserProfileRepository) {

    suspend operator fun invoke(userProfileWithAllergens: UserProfileWithAllergens): Result<Unit> {
        return userProfileRepository.saveUserProfile(userProfileWithAllergens)
    }
}