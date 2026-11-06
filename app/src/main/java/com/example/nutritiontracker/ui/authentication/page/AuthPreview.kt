package com.example.nutritiontracker.ui.authentication.page

import androidx.compose.runtime.Composable
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthContentType
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthViewModelCallback
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationUiState
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.utils.composable.DevicePreviews


@DevicePreviews
@Composable
fun SignInPreview() {

    AuthPage(
        signInForm = FormBuilder(),
        signUpForm = FormBuilder(),
        authCallback = object : AuthViewModelCallback {
            override fun signIn() {
                TODO("Not yet implemented")
            }

            override fun signUp() {
                TODO("Not yet implemented")
            }

            override fun onContentTypeChange(authContentType: AuthContentType) {
                TODO("Not yet implemented")
            }

        }
    )
}

@DevicePreviews
@Composable
fun SignUpPreview() {

    AuthPage(
        signInForm = FormBuilder(),
        signUpForm = FormBuilder(),
        uiState = AuthenticationUiState(contentType = AuthContentType.SIGN_UP),
        authCallback = object : AuthViewModelCallback {
            override fun signIn() {
                TODO("Not yet implemented")
            }

            override fun signUp() {
                TODO("Not yet implemented")
            }

            override fun onContentTypeChange(authContentType: AuthContentType) {
                TODO("Not yet implemented")
            }

        }
    )
}