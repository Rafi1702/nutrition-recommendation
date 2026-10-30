package com.example.nutritiontracker.domain.usecase

import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.repository.UserProfileRepository
import dagger.assisted.Assisted
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
//    @Assisted private val getAllergensUsecase: GetAllergensUseCase
) {

    //TODO: INJECT GetAllergensUseCase later to merge it with UserProfile
    suspend operator fun invoke(): Result<UserProfile?> {
        return userProfileRepository.getUserProfile()
    }
}