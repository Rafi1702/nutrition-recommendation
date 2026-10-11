package com.example.nutritiontracker.ui.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritiontracker.domain.model.SignInAuth
import com.example.nutritiontracker.domain.model.SignUpAuth
import com.example.nutritiontracker.domain.usecase.SignInUseCase
import com.example.nutritiontracker.domain.usecase.SignUpUseCase
import com.example.nutritiontracker.ui.components.CheckRequiredValidator
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


data class SignInForm(
    val username: CharSequence,
    val password: CharSequence,
    val isChecked: Boolean,
)

data class SignUpForm(
    val username: CharSequence,
    val password: CharSequence,
    val confirmPassword: CharSequence,
)

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
) : ViewModel(), AuthViewModelCallback {

    val signInForm = FormBuilder<SignInForm>(persist = true).apply {
        addField(
            SignInForm::username, FieldProperties(
                validator = EmailValidator(),
                isRequired = true,
            )
        )
        addField(
            SignInForm::password,
            FieldProperties(
                validator = PasswordValidator(),
                isRequired = true
            ),
        )

        addField(
            SignInForm::isChecked,
            FieldProperties(
                validator = CheckRequiredValidator()
            )
        )
    }

    val signUpForm = FormBuilder<SignUpForm>().apply{
        addField(
            SignUpForm::username, FieldProperties(
                persist = true,
                validator = EmailValidator(),
                isRequired = true,
            )
        )

        addField(
            SignUpForm::password,
            FieldProperties(
                persist = true,
                validator = PasswordValidator(),
                isRequired = true
            ),
        )

        addField(
            SignUpForm::confirmPassword,
            FieldProperties(
                persist = true,
                validator = MatchValidator(
                    form = this,
                    targetFieldKey = SignUpForm::password,
                ),
                isRequired = true
            ),
        )
    }

    private val _uiState = MutableStateFlow(AuthenticationUiState())

    val uiState = _uiState.asStateFlow()


    override fun signIn() {
        viewModelScope.launch {
            Log.d("[SIGN_IN]", "PRESSED SIGN IN")
            val username = signInForm.getField<CharSequence>(SignInForm::username)?.valueState?.value
            val password = signInForm.getField<CharSequence>(SignInForm::password)?.valueState?.value

            if (username != null && password != null) {
                _uiState.update { it.copy(isLoading = true) }
                delay(1000)
                signInUseCase(
                    SignInAuth(
                        username = username.toString(),
                        password = password.toString()
                    )
                ).fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                    },
                    onFailure = { err ->
                        _uiState.update { it.copy(error = err.message, isLoading = false) }
                    }
                )
            }
        }
    }

    override fun signUp() {
        viewModelScope.launch {
            val username = signUpForm.getField<String>(SignUpForm::username)?.valueState?.value
            val password = signUpForm.getField<String>(SignUpForm::password)?.valueState?.value
            val confirmPassword = signUpForm.getField<String>(SignUpForm::confirmPassword)?.valueState?.value

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

    override fun onContentTypeChange(authContentType: AuthContentType) {
        viewModelScope.launch {
            _uiState.update { it.copy(contentType = authContentType) }
        }
    }
}

internal interface AuthViewModelCallback{
    fun signIn()
    fun signUp()
    fun onContentTypeChange(authContentType: AuthContentType)
}



