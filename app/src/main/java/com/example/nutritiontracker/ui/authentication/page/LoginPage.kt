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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.CheckBoxForm
import com.example.nutritiontracker.ui.components.CheckRequiredValidator
import com.example.nutritiontracker.ui.components.EmailValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.MatchValidator
import com.example.nutritiontracker.ui.components.PasswordValidator
import com.example.nutritiontracker.ui.components.TextFormField
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

private enum class AuthContentType {
    SIGN_IN,
    SIGN_UP
}

@Preview(showBackground = true)
@Composable
internal fun LoginPage(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {}
) {
    var authContent by remember { mutableStateOf(AuthContentType.SIGN_UP) }

    NutritionTrackerTheme {
        Scaffold(
            containerColor = colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .sizeIn(
                        maxWidth = 480.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    24.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                Text("APP LOGO", style = typography.displaySmall)
                when (authContent) {
                    AuthContentType.SIGN_IN -> {
                        SignInContent(
                            onSignUpPressed = {
                                authContent = it
                            },
                            onSignInButtonPressed = {
                                onNavigateToHome()
                                Log.d("[SIGN_IN]", "BUTTON_PRESSED")
                            }
                        )
                    }

                    AuthContentType.SIGN_UP -> {
                        SignUpContent(onSignInPressed = { authContent = it })
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
        shape = RoundedCornerShape(16.dp),
        color = colorScheme.surfaceVariant,
        contentColor = colorScheme.onSurfaceVariant,
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.08f))
    ) {
        Form(modifier = Modifier.padding(20.dp), verticalSpacing = 16.dp) { isValid, form ->
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                formBuilder = form,
                label = "Password",
                fieldName = "password_sign_up",
                initialValue = "TESST",
                backgroundColor = colorScheme.surface,
                fieldProperties = FieldProperties(
                    validator = PasswordValidator(),
                    isRequired = true,
                ),
            )
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                formBuilder = form,
                fieldName = "password_sign_up_confirm",
                label = "Confirm password",
                initialValue = "",
                backgroundColor = colorScheme.surface,
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
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Already have an account?",
                    style = typography.bodyMedium.copy(color = colorScheme.onSurfaceVariant)
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
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
                        "password: ${password?.valueProvider?.invoke()}, confirmed_password: ${confirmedPassword?.valueProvider?.invoke()}"
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
        shape = RoundedCornerShape(16.dp),
        color = colorScheme.surfaceVariant,
        contentColor = colorScheme.onSurfaceVariant,
        border = BorderStroke(1.dp, colorScheme.onSurface.copy(alpha = 0.08f))
    ) {
        Form(modifier = Modifier.padding(20.dp), verticalSpacing = 16.dp) { isValid, form ->
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldProperties = FieldProperties(
                    validator = EmailValidator(),
                    isRequired = true
                ),
                fieldName = "email",
                formBuilder = form,
                label = "Email",
                backgroundColor = colorScheme.surface,
            )

            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldProperties = FieldProperties(
                    validator = PasswordValidator(),
                    isRequired = true,
                ),
                fieldName = "password",
                formBuilder = form,
                label = "Password",
                backgroundColor = colorScheme.surface,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
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
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Doesn't have an account?",
                    style = typography.bodyMedium.copy(color = colorScheme.onSurfaceVariant)
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
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
