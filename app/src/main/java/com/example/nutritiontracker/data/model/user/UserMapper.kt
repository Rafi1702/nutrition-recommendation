package com.example.nutritiontracker.data.model.user

import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.domain.model.UserDailyNeeds
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.GenderMap.toGender
import com.example.nutritiontracker.domain.model.PersonalActivitiesMap.toPersonalActivities
import com.example.nutritiontracker.domain.model.UserProfileWithAllergens


internal fun UserProfileResponseDto.toUserProfile(): UserProfile {
    return UserProfile(
        gender = this.gender.toGender(),
        dailyActivity = PersonalActivities.SEDENTARY,
        weight = this.weight,
        age = this.age,
        height = this.height,
        needs = UserDailyNeeds(
            breakfast = this.needs.breakfast,
            lunch = this.needs.lunch,
            dinner = this.needs.dinner
        ),
        birthDate = this.birth,
        allergenIds = this.allergenIds
    )
}

internal fun UserProfile.toCreateUserProfile(): UserProfileUpdateCreateRequestDto {
    return UserProfileUpdateCreateRequestDto(
        gender = this.gender.label,
        personalActivities = this.dailyActivity.label,
        birth = this.birthDate,
        weight = this.weight,
        height = this.height,
        userAllergies = this.allergenIds,
    )
}

internal fun UserProfileLocal.toUserProfile(): UserProfile {
    return UserProfile(
        gender = this.gender.toGender(),
        dailyActivity = this.personalActivities.toPersonalActivities(),
        birthDate = this.birth,
        weight = this.weight,
        height = this.height,
        allergenIds = this.userAllergiesIds
    )
}

internal fun UserProfileWithAllergens.toSaveUserProfileLocal(): UserProfileLocal {
    return UserProfileLocal(
        gender = this.userProfile.gender.label,
        personalActivities = this.userProfile.dailyActivity.label,
        birth = this.userProfile.birthDate,
        weight = this.userProfile.weight,
        height = this.userProfile.height,
        userAllergiesIds = this.allergens.map { it.id },
    )
}