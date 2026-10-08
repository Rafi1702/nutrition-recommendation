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
import com.example.nutritiontracker.ui.components.CheckBoxForm
import com.example.nutritiontracker.ui.components.CheckRequiredValidator
import com.example.nutritiontracker.ui.components.EmailValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.MatchValidator
import com.example.nutritiontracker.ui.components.PasswordValidator
import com.example.nutritiontracker.ui.components.textfield.TextFormField
import com.example.nutritiontracker.ui.theme.LocalForm
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

    val authContentType = authViewModel.contentType.collectAsState()
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
                when (authContentType.value) {
                    AuthContentType.SIGN_IN -> {
                        SignInContent(
                            onSignUpPressed = {
                                authViewModel.onContentTypeChange(AuthContentType.SIGN_UP)
                            },
                            onSignInButtonPressed = {
                                onNavigateToHome()
                                Log.d("[SIGN_IN]", "BUTTON_PRESSED")
                            }
                        )
                    }

                    AuthContentType.SIGN_UP -> {
                        SignUpContent(onSignInPressed = {
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
private fun SignUpContent(onSignInPressed: (AuthContentType) -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 6.dp,
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(Spacing.m),
        color = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.08f))
    ) {
        Form(modifier = Modifier.padding(Spacing.m), verticalSpacing = Spacing.m) { isValid ->
            val form = LocalForm.current
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                label = "Password",
                fieldName = "password_sign_up",
                initialValue = "TESST",
                backgroundColor = colorScheme.surfaceVariant,
                fieldProperties = FieldProperties(
                    validator = PasswordValidator(),
                    isRequired = true,
                ),
            )
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = "password_sign_up_confirm",
                label = "Confirm password",
                initialValue = "",
                backgroundColor = colorScheme.surfaceVariant,
                fieldProperties = FieldProperties(
                    validator = MatchValidator(
                        form = form,
                        targetFieldKey = "password_sign_up"
                    ),
                    isRequired = true
                ),
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
                    val (password, confirmedPassword) = (form.getField<CharSequence>("password_sign_up") to form.getField<CharSequence>(
                        "password_sign_up_confirm"
                    ))

                    Log.d(
                        "[PRESSED_BUTTON]",
                        "password: ${password?.valueState?.value}, confirmed_password: ${confirmedPassword?.valueState?.value}"
                    )
                    /* TODO: Sign Up */
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
    onSignInButtonPressed: () -> Unit = {}
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
        Form(modifier = Modifier.padding(Spacing.m), verticalSpacing = Spacing.m) { isValid ->
            val form = LocalForm.current
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldProperties = FieldProperties(
                    validator = EmailValidator(),
                    isRequired = true
                ),
                fieldName = "email",
                label = "Email",
                backgroundColor = colorScheme.surfaceVariant,
            )

            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldProperties = FieldProperties(
                    validator = PasswordValidator(),
                    isRequired = true,
                ),
                fieldName = "password",
                label = "Password",
                backgroundColor = colorScheme.surfaceVariant,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckBoxForm(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    form = form,
                    name = "remember",
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
                Text("Sign In", style = typography.labelLarge)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}
