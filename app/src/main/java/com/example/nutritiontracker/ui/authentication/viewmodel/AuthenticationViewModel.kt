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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class AuthContentType {
    SIGN_IN,
    SIGN_UP
}


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    val form = FormBuilder()

    private val _contentType = MutableStateFlow(AuthContentType.SIGN_IN)

    val contentType = _contentType.asStateFlow()

    operator fun component1(): StateFlow<AuthContentType> {
        return contentType
    }

    init {
        viewModelScope.launch {
            _contentType.collect { content ->
                if (form.fieldRegistry.isNotEmpty()) form.clear()
                when (content) {
                    AuthContentType.SIGN_IN -> {
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
                signInUseCase(
                    SignInAuth(
                        username = username,
                        password = password
                    )
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
            _contentType.update { authContentType }
        }
    }
}



