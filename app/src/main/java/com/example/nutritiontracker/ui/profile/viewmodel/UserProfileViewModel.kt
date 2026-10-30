package com.example.nutritiontracker.ui.profile.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.Allergens
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.usecase.GetAllergensUseCase
import com.example.nutritiontracker.domain.usecase.GetUserProfileUseCase
import com.example.nutritiontracker.ui.components.CannotEmptyList
import com.example.nutritiontracker.ui.components.CannotEmptyValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.MockNumberValidator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class UserProfileForm(
    val gender: String,
    val personalActivities: String,
    val birth: String,
    val weight: Float,
    val height: Float,
    val userAllergies: List<String>
)


data class AllergensState(
    val isLoading: Boolean = false,
    val allergens: List<Allergens> = emptyList(),
    val error: String? = null,
)

data class UserProfileUiState(
    val isLoadingMutation: Boolean = false,
    val allergensState: AllergensState = AllergensState()
)


@HiltViewModel(assistedFactory = UserProfileViewModel.Factory::class)
class UserProfileViewModel @AssistedInject constructor(
    private val getAllergensUseCase: GetAllergensUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    @Assisted private val userProfile: UserProfile? = null
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState = _uiState.asStateFlow()

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
            )
        )
    }.apply{
        if (userProfile != null) {
            this.setFieldValue(UserProfileForm::gender, "Male")
            this.setFieldValue(
                UserProfileForm::personalActivities,
                userProfile.dailyActivity
            )
            this.setFieldValue(UserProfileForm::weight, userProfile.weight)
            this.setFieldValue(UserProfileForm::height, userProfile.height)
            this.setFieldValue(UserProfileForm::birth, userProfile.birthDate)
            this.setFieldValue(UserProfileForm::userAllergies, listOf("Peanut"))
        }
    }

    init {
        getAllergens()
    }

    private fun getAllergens() {
        viewModelScope.launch {
            _uiState.update { it.copy(allergensState = it.allergensState.copy(isLoading = true)) }
            delay(3000)
            
            getAllergensUseCase().fold(
                onSuccess = { data ->
                    _uiState.update { it.copy(allergensState = it.allergensState.copy(allergens = data)) }
                }, onFailure = { err ->
                    _uiState.update { it.copy(allergensState = it.allergensState.copy(error = err.message)) }
                }
            )


            _uiState.update {
                it.copy(
                    allergensState = it.allergensState.copy(isLoading = false)
                )
            }

        }
    }
}