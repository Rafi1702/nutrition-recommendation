package com.example.nutritiontracker.ui.authentication.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthContentType
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthViewModelCallback
import com.example.nutritiontracker.ui.authentication.viewmodel.SignInForm
import com.example.nutritiontracker.ui.components.FormBuilder

@Preview(widthDp = 840)
@Composable
fun SignInPreview() {

    AuthPage(
        signInForm = FormBuilder(),
        signUpForm = FormBuilder(),
        authCallback = object : AuthViewModelCallback{
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