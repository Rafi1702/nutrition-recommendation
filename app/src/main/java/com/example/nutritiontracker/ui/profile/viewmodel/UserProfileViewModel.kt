package com.example.nutritiontracker.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.usecase.GetUserProfileUseCase
import com.example.nutritiontracker.ui.components.CannotEmptyValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.MockNumberValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


import javax.inject.Inject



sealed interface Resource<out T> {
    object Idle : Resource<Nothing>
    object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable) : Resource<Nothing>
}

data class UserProfileForm(
    val gender: String,
    val birth: String,
    val weight: Double,
    val height: Double,
)

data class UserProfileUiState(
    val queryState: Resource<UserProfile>,
    val mutationState: Resource<UserProfile>
)


@HiltViewModel
class UserProfileViewModel @Inject constructor(private val getUserProfile: GetUserProfileUseCase) :
    ViewModel() {
    val userProfileForm = FormBuilder<UserProfileForm>().apply {
        addField(
            UserProfileForm::gender, FieldProperties(
                validator = CannotEmptyValidator(),
                initialValue = "Male"
            )
        )

        addField(
            name = UserProfileForm::birth, FieldProperties(
                validator = CannotEmptyValidator()
            )
        )

        addField(
            name = UserProfileForm::weight, FieldProperties(
                validator = MockNumberValidator()
            )
        )

        addField(
            name = UserProfileForm::height, FieldProperties(
                validator = MockNumberValidator()
            )
        )
    }

    fun getUserProfile() {
        viewModelScope.launch{

        }
    }
}