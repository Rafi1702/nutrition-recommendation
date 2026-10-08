package com.example.nutritiontracker.ui.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth
import com.example.nutritiontracker.domain.usecase.SignInUseCase
import com.example.nutritiontracker.domain.usecase.SignUpUseCase
import com.example.nutritiontracker.ui.components.EmailValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.MatchValidator
import com.example.nutritiontracker.ui.components.PasswordValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class AuthContentType {
    SIGN_IN,
    SIGN_UP
}

data class AuthenticationUiState(
    val error: String? = null,
    val contentType: AuthContentType = AuthContentType.SIGN_IN,
    val isLoading: Boolean = false
)

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    val form = FormBuilder().apply {
        addField(
            "username", FieldProperties(
                validator = EmailValidator(),
                isRequired = true
            )
        )

        addField(
            "password",
            FieldProperties(
                validator = PasswordValidator(),
                isRequired = true
            ),
        )
    }

    private val _uiState = MutableStateFlow(AuthenticationUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.collect {
                when (it.contentType) {
                    AuthContentType.SIGN_IN -> {
                        form.clear()
                        form.addField(
                            mapOf(
                                "username" to FieldProperties(
                                    validator = EmailValidator(),
                                    isRequired = true
                                ),
                                "password" to FieldProperties(
                                    validator = PasswordValidator(),
                                    isRequired = true
                                ),
                            )
                        )
                    }

                    AuthContentType.SIGN_UP -> {
                        form.clear()
                        form.addField(
                            mapOf(
                                "username" to FieldProperties(
                                    validator = EmailValidator(),
                                    isRequired = true
                                ),
                                "password" to FieldProperties(
                                    validator = PasswordValidator(),
                                    isRequired = true
                                ),
                                "confirm_password" to FieldProperties(
                                    validator = MatchValidator(
                                        form = form,
                                        targetFieldKey = "password"
                                    ),
                                    isRequired = true
                                ),
                            )
                        )
                    }
                }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val username = form.getField<String>("username")?.valueState?.value
            val password = form.getField<String>("password")?.valueState?.value

            if (username != null && password != null) {
                _uiState.update { it.copy(isLoading = true) }
                delay(1000)
                signInUseCase(
                    SignInAuth(
                        username = username,
                        password = password
                    )
                ).fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                    },
                    onFailure = {
                        _uiState.update { it.copy(error = it.error) }
                    }
                )
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            val username = form.getField<String>("username")?.valueState?.value
            val password = form.getField<String>("password")?.valueState?.value
            val confirmPassword = form.getField<String>("confirm_password")?.valueState?.value

            if (username != null && password != null && confirmPassword != null) {
                signUpUseCase(
                    SignUpAuth(
                        username = username,
                        password = password,
                        confirmPassword = confirmPassword
                    )
                )
            }
        }
    }

    fun onContentTypeChange(authContentType: AuthContentType) {
        viewModelScope.launch {
            _uiState.update { it.copy(contentType = authContentType) }
        }
    }
}



