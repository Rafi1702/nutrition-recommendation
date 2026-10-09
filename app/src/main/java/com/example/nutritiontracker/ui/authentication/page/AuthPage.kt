package com.example.nutritiontracker.ui.authentication.page

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthContentType
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationViewModel
import com.example.nutritiontracker.ui.authentication.viewmodel.SignInForm
import com.example.nutritiontracker.ui.authentication.viewmodel.SignUpForm
import com.example.nutritiontracker.ui.components.CheckBoxForm
import com.example.nutritiontracker.ui.components.CheckRequiredValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.textfield.TextFormField
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography


@Composable
internal fun AuthPage(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {},
    authViewModel: AuthenticationViewModel,
) {

    val uiState = authViewModel.uiState.collectAsState()

    NutritionTrackerTheme {
        Scaffold(
            containerColor = colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(Spacing.m)
                    .sizeIn(
                        maxWidth = 480.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    Spacing.l,
                    alignment = Alignment.CenterVertically
                )
            ) {
                Text("APP LOGO", style = typography.displaySmall)
                when (uiState.value.contentType) {
                    AuthContentType.SIGN_IN -> {
                        SignInContent(
                            formBuilder = authViewModel.signInForm,
                            onSignUpPressed = {
                                authViewModel.onContentTypeChange(AuthContentType.SIGN_UP)
                            },
                            onSignInButtonPressed = {
                                authViewModel.signIn()
                                Log.d("[SIGN_IN]", "BUTTON_PRESSED")
                            }
                        )
                    }

                    AuthContentType.SIGN_UP -> {
                        SignUpContent(formBuilder = authViewModel.signUpForm, onSignInPressed = {
                            authViewModel.onContentTypeChange(
                                AuthContentType.SIGN_IN
                            )
                        })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContent(
    onSignInPressed: (AuthContentType) -> Unit = {},
    formBuilder: FormBuilder<SignUpForm>? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 6.dp,
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(Spacing.m),
        color = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.08f))
    ) {
        Form(
            modifier = Modifier.padding(Spacing.m),
            verticalSpacing = Spacing.m,
            isPersist = true,
            formBuilder = formBuilder
        ) { isValid ->
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                label = "Password",
                fieldName = SignUpForm::password,
                persist = true,
                backgroundColor = colorScheme.surfaceVariant
            )
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignUpForm::confirmPassword,
                label = "Confirm password",
                persist = true,
                backgroundColor = colorScheme.surfaceVariant,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    text = "Already have an account?",
                    style = typography.bodyMedium.copy(color = colorScheme.onSurface)
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Spacing.none) {
                    TextButton(
                        contentPadding = PaddingValues(Spacing.none),
                        onClick = { onSignInPressed(AuthContentType.SIGN_IN) },
                        colors = ButtonDefaults.textButtonColors(contentColor = colorScheme.primary)
                    ) {
                        Text(
                            text = "Sign In",
                            style = typography.labelLarge.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                },
                shape = RoundedCornerShape(8.dp),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                )
            ) {
                Text("Sign Up", style = typography.labelLarge)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInContent(
    onSignUpPressed: (AuthContentType) -> Unit = {},
    onSignInButtonPressed: () -> Unit = {},
    isLoading: Boolean = false,
    formBuilder: FormBuilder<SignInForm>? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 6.dp,
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(Spacing.m),
        color = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.08f))
    ) {
        Form(
            modifier = Modifier.padding(Spacing.m),
            formBuilder = formBuilder,
            isPersist = true,
            verticalSpacing = Spacing.m
        ) { isValid ->

            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignInForm::username,
                label = "Email",
                persist = true,
                backgroundColor = colorScheme.surfaceVariant,
            )

            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignInForm::password,
                label = "Password",
                persist = true,
                backgroundColor = colorScheme.surfaceVariant,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckBoxForm(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    fieldName = SignInForm::isChecked,
                    fieldProperties = FieldProperties(
                        validator = CheckRequiredValidator(),
                        isRequired = true
                    ),
                    onCheckedChange = { },
                    removePadding = true,
                )
                Text(
                    text = "Remember me",
                    style = typography.bodyMedium.copy(color = colorScheme.onSurface)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    text = "Doesn't have an account?",
                    style = typography.bodyMedium.copy(color = colorScheme.onSurface)
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Spacing.none) {
                    TextButton(
                        contentPadding = PaddingValues(Spacing.none),
                        onClick = { onSignUpPressed(AuthContentType.SIGN_UP) },
                        colors = ButtonDefaults.textButtonColors(contentColor = colorScheme.primary)
                    ) {
                        Text(
                            text = "Sign Up",
                            style = typography.labelLarge.copy(
                                color = colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Button(
                onClick = onSignInButtonPressed,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                )
            ) {
                if (isLoading) CircularProgressIndicator() else Text(
                    "Sign In",
                    style = typography.labelLarge
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}
