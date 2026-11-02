package com.example.nutritiontracker.domain.usecase.user_profile

import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
) {

    //TODO: INJECT GetAllergensUseCase later to merge it with UserProfile
    suspend operator fun invoke(): Result<UserProfile?> {
        return userProfileRepository.getUserProfile()
    }
}