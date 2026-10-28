package com.example.nutritiontracker.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.usecase.GetUserProfileUseCase
import com.example.nutritiontracker.ui.components.CannotEmptyList
import com.example.nutritiontracker.ui.components.CannotEmptyValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.MockNumberValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


import javax.inject.Inject


import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.domain.model.PersonalActivitiesMap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

sealed interface Resource<out T> {
    object Idle : Resource<Nothing>
    object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable) : Resource<Nothing>
}

data class UserProfileForm(
    val gender: String,
    val personalActivities: String,
    val birth: String,
    val weight: Float,
    val height: Float,
    val userAllergies: List<String>
)

data class UserProfileUiState(
    val queryState: Resource<UserProfile>,
    val mutationState: Resource<UserProfile>
)


@HiltViewModel(assistedFactory = UserProfileViewModel.Factory::class)
class UserProfileViewModel @AssistedInject constructor(
    private val getUserProfile: GetUserProfileUseCase,
    @Assisted private val userProfile: UserProfile? = null
) :
    ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userProfile: UserProfile? = null): UserProfileViewModel
    }

    val userProfileForm = FormBuilder<UserProfileForm>().apply {
        addField(
            UserProfileForm::gender, FieldProperties(
                validator = CannotEmptyValidator(),
                initialValue = "Male",
                isRequired = true
            )
        )

        addField(
            UserProfileForm::personalActivities, FieldProperties(
                validator = CannotEmptyValidator(),
                isRequired = true
            )
        )

        addField(
            name = UserProfileForm::birth, FieldProperties(
                validator = CannotEmptyValidator()
            )
        )

        addField(
            name = UserProfileForm::weight, FieldProperties(
                validator = MockNumberValidator(),
                isRequired = true
            )
        )

        addField(
            name = UserProfileForm::height, FieldProperties(
                validator = MockNumberValidator(),
                isRequired = true
            )
        )

        addField(
            name = UserProfileForm::userAllergies, FieldProperties(
                validator = CannotEmptyList(),
                initialValue = listOf("Peanut"),
                isRequired = true
            )
        )
    }

    init {
        if (userProfile != null) {
            userProfileForm.setFieldValue(UserProfileForm::gender, "Male")
            userProfileForm.setFieldValue(
                UserProfileForm::personalActivities,
                userProfile.dailyActivity
            )
            userProfileForm.setFieldValue(UserProfileForm::weight, userProfile.weight)
            userProfileForm.setFieldValue(UserProfileForm::height, userProfile.height)
            userProfileForm.setFieldValue(UserProfileForm::birth, userProfile.birthDate)
        }
    }
}