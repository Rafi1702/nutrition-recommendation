package com.example.nutritiontracker.data.model.user

import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.toGender

internal fun UserProfileResponseDto.toUserProfile(): UserProfile {
    return UserProfile(
        gender = this.gender.toGender(),
        dailyActivity = PersonalActivities.SEDENTARY,
        weight = this.weight,
        age = this.age,
        height = this.height,
    )
}