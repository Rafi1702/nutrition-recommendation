package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import javax.inject.Inject

class CreateUserProfileUseCase @Inject constructor(private val userProfileRepository: UserProfileRepository) {
    suspend operator  fun invoke(userProfile: UserProfile): Result<Unit> {
        return userProfileRepository.createUserProfile(userProfile)
    }
}