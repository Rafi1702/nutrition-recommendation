package com.example.nutritiontracker.domain.usecase

import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val userProfileRepository: UserProfileRepository) {
    suspend operator fun invoke(): Result<UserProfile>{
        return userProfileRepository.getUserProfile()
    }
}