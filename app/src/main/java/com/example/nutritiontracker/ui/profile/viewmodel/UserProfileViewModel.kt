package com.example.nutritiontracker.ui.profile.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.Allergens
import com.example.nutritiontracker.domain.model.PersonalActivitiesMap.toPersonalActivities
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.domain.model.toGender
import com.example.nutritiontracker.domain.usecase.CreateUserProfileUseCase
import com.example.nutritiontracker.domain.usecase.GetAllergensUseCase
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

data class UserProfileMutationState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class UserProfileUiState(
    val allergensState: AllergensState = AllergensState(),
    val userProfileMutationState: UserProfileMutationState = UserProfileMutationState()
)

@HiltViewModel(assistedFactory = UserProfileViewModel.Factory::class)
class UserProfileViewModel @AssistedInject constructor(
    private val getAllergensUseCase: GetAllergensUseCase,
    private val createUserProfileUseCase: CreateUserProfileUseCase,
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
                validator = CannotEmptyValidator(),
                isRequired = true
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
        addField(
            name = UserProfileForm::birth,
            FieldProperties(
                validator = CannotEmptyValidator(),
                isRequired = true
            ),
        )
    }.apply {
        if (userProfile != null) {
            this.setFieldValue(UserProfileForm::gender, "Male")
            this.setFieldValue(
                UserProfileForm::personalActivities,
                userProfile.dailyActivity
            )
            this.setFieldValue(UserProfileForm::weight, userProfile.weight)
            this.setFieldValue(UserProfileForm::height, userProfile.height)
            this.setFieldValue(UserProfileForm::birth, userProfile.birthDate)
        }
    }

    init {
        getAllergens()
    }

    private fun getAllergens() {
        viewModelScope.launch {
            _uiState.update { it.copy(allergensState = it.allergensState.copy(isLoading = true)) }
            delay(3000)

            val allergens = getAllergensUseCase().getOrElse { err ->
                _uiState.update {
                    it.copy(
                        allergensState = it.allergensState.copy(
                            error = err.message,
                            isLoading = false
                        )
                    )
                }
                return@getOrElse emptyList()
            }

            val userAllergenIds = userProfile?.allergenIds ?: emptyList()

            val initialAllergensValue = userAllergenIds.map { userAllergies ->
                allergens.first { it.id == userAllergies }
            }

            userProfileForm.setFieldValue(UserProfileForm::userAllergies, initialAllergensValue)

            _uiState.update { it.copy(allergensState = it.allergensState.copy(allergens = if (userProfile != null) initialAllergensValue else allergens)) }

            _uiState.update {
                it.copy(
                    allergensState = it.allergensState.copy(isLoading = false)
                )
            }
        }
    }

    private fun userProfileValue(): UserProfile? {
        val isFormComplete = listOf(
            UserProfileForm::gender,
            UserProfileForm::personalActivities,
            UserProfileForm::weight,
            UserProfileForm::height,
            UserProfileForm::birth
        ).all { key -> userProfileForm.getField(key)!!.state.value != null }

        if (isFormComplete) {
            return UserProfile(
                gender = userProfileForm.getField(UserProfileForm::gender)!!.state.value!!.toGender(),
                dailyActivity = userProfileForm.getField(UserProfileForm::personalActivities)!!.state.value!!.toPersonalActivities(),
                birthDate = userProfileForm.getField(UserProfileForm::birth)!!.state.value!!,
                weight = userProfileForm.getField(UserProfileForm::weight)!!.state.value!!,
                height = userProfileForm.getField(UserProfileForm::height)!!.state.value!!,
                allergenIds = userProfileForm.getField(UserProfileForm::userAllergies)!!.state.value!!.map { allergenName ->
                    _uiState.value.allergensState.allergens.filter { it.name == allergenName }
                        .first().id
                },
            )
        }
        return null
    }

    private fun createUserProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    userProfileMutationState = it.userProfileMutationState.copy(
                        isLoading = true
                    )
                )
            }
            delay(300)


            userProfileValue()?.let { profile ->
                createUserProfileUseCase(
                    profile
                ).fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                userProfileMutationState = it.userProfileMutationState.copy(
                                    isLoading = false
                                )
                            )
                        }
                    },
                    onFailure = { err ->
                        _uiState.update {
                            it.copy(
                                userProfileMutationState = it.userProfileMutationState.copy(
                                    isLoading = false,
                                    error = err.message
                                )
                            )
                        }
                    }
                )
            }


        }
    }
}