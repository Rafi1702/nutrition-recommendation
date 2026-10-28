package com.example.nutritiontracker.ui.authentication.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.UserProfile
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthContentType
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthViewModelCallback
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationUiState
import com.example.nutritiontracker.ui.authentication.viewmodel.AuthenticationViewModel
import com.example.nutritiontracker.ui.authentication.viewmodel.SignInForm
import com.example.nutritiontracker.ui.authentication.viewmodel.SignUpForm
import com.example.nutritiontracker.ui.components.CheckBoxForm
import com.example.nutritiontracker.ui.components.CheckRequiredValidator
import com.example.nutritiontracker.ui.components.EmailValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.FormBuilder
import com.example.nutritiontracker.ui.components.textfield.TextFormField
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.theme.userProfile


@Composable
internal fun AuthPageRoute(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    onNavigateToHome: (userProfile: UserProfile?) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(uiState.value.success){
        if(uiState.value.success == true){
            val userProfile = viewModel.getUserProfile()
            onNavigateToHome(userProfile)
        }
    }

    AuthPage(
        uiState = uiState.value,
        signInForm = viewModel.signInForm,
        signUpForm = viewModel.signUpForm,
        authCallback = viewModel
    )
}

@Composable
internal fun AuthPage(
    modifier: Modifier = Modifier,
    uiState: AuthenticationUiState = AuthenticationUiState(),
    signInForm: FormBuilder<SignInForm>,
    signUpForm: FormBuilder<SignUpForm>,
    authCallback: AuthViewModelCallback,
) {
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

                Surface(
                    shadowElevation = 8.dp,
                    shape = CircleShape,
                    color = colorScheme.surfaceVariant
                ) {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(Spacing.l),
                        painter = painterResource(R.drawable.ic_nutritracker_logo_altenative),
                        contentDescription = null
                    )
                }

                when (uiState.contentType) {
                    AuthContentType.SIGN_IN -> {
                        SignInContent(
                            formBuilder = signInForm,
                            onSignUpPressed = {
                                authCallback.onContentTypeChange(AuthContentType.SIGN_UP)
                            },
                            errorMessage = uiState.error,
                            onSignInButtonPressed = {
                                authCallback.signIn()
                            },
                            isLoading = uiState.isLoading,
                        )
                    }

                    AuthContentType.SIGN_UP -> {
                        SignUpContent(
                            formBuilder = signUpForm,
                            onSignUpButtonPressed = {
                                authCallback.signUp()
                            },
                            onSignInPressed = {
                                authCallback.onContentTypeChange(
                                    AuthContentType.SIGN_IN
                                )
                            },
                            isLoading = uiState.isLoading,
                            errorMessage = uiState.error
                        )
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
    onSignUpButtonPressed: () -> Unit = {},
    formBuilder: FormBuilder<SignUpForm>? = null,
    isLoading: Boolean = false,
    errorMessage: String? = null,
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
            formBuilder = formBuilder
        ) { isValid ->
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignUpForm::username,
                label = "Username",
                prefix = { Icon(Icons.Default.Person, contentDescription = "user_username") },
                placeholder = { Text(stringResource(R.string.username_auth_textfield_placeholder)) },
                backgroundColor = colorScheme.surfaceVariant,
            )
            val passwordField = rememberPasswordVisibility()
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                label = "Password",
                isSecure = !passwordField.isVisible,
                fieldName = SignUpForm::password,
                backgroundColor = colorScheme.surfaceVariant,
                prefix = {
                    passwordField.Prefix()
                },
                placeholder = {
                    Text(stringResource(R.string.password_auth_textfield_placeholder))
                },

                suffix = {
                    passwordField.Suffix()
                }
            )
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignUpForm::confirmPassword,
                label = "Confirm password",
                placeholder = { Text("Input confirm password") },
                isSecure = !passwordField.isVisible,
                backgroundColor = colorScheme.surfaceVariant,
                prefix = {
                    passwordField.Prefix()
                },
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
                onClick = onSignUpButtonPressed,
                shape = RoundedCornerShape(8.dp),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                enabled = isValid && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(Spacing.m))
                } else {
                    Text("Sign Up", style = typography.labelLarge)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }

            Box(modifier = Modifier.height(16.dp)) {
                errorMessage?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Dangerous,
                            contentDescription = "content_error",
                            tint = colorScheme.error
                        )
                        Text(
                            text = errorMessage,
                            style = typography.labelLarge.copy(color = colorScheme.error)
                        )
                    }
                }
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
    errorMessage: String? = null,
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
            verticalSpacing = Spacing.m
        ) { isValid ->

            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignInForm::username,
                label = "Username",
                backgroundColor = colorScheme.surfaceVariant,
                prefix = { Icon(Icons.Default.Person, contentDescription = "user_username") },
                placeholder = { Text(stringResource(R.string.username_auth_textfield_placeholder)) },
            )


            val passwordField = rememberPasswordVisibility()
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldName = SignInForm::password,
                label = "Password",
                backgroundColor = colorScheme.surfaceVariant,
                suffix = {
                    passwordField.Suffix()
                },
                prefix = { Icon(Icons.Default.Lock, contentDescription = "user_password") },
                placeholder = { Text(stringResource(R.string.password_auth_textfield_placeholder)) },
                isSecure = !passwordField.isVisible,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckBoxForm(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    fieldName = SignInForm::isChecked,
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
                enabled = isValid && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(Spacing.m))
                } else {
                    Text(
                        "Sign In",
                        style = typography.labelLarge
                    )
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        "atau masuk dengan",
                        modifier = Modifier.weight(1f),
                        style = typography.labelSmall.copy(textAlign = TextAlign.Center)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
            }

            Box(modifier = Modifier.height(16.dp)) {
                errorMessage?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Dangerous,
                            contentDescription = "content_error",
                            tint = colorScheme.error
                        )
                        Text(
                            text = errorMessage,
                            style = typography.labelLarge.copy(color = colorScheme.error)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThirdPartyAuthButton(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(8.dp)
    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            shape = shape,
            colors = ButtonDefaults.buttonColors()
                .copy(
                    containerColor = colorScheme.surfaceVariant,
                    contentColor = colorScheme.onSurfaceVariant
                ),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
            Icon(Icons.Default.AutoFixHigh, contentDescription = "google_auth")
            Spacer(modifier = Modifier.width(Spacing.s))
            Text("Google")
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape = shape)
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .rotate(-45f)
                    .offset(y = (-4).dp)
                    .clip(shape),
                color = colorScheme.tertiaryContainer
            ) {
                Text(
                    "Coming Soon",
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 2.dp),
                    style = typography.labelSmall.copy(fontSize = 8.sp)
                )
            }

        }
    }
}

private data class PasswordField(
    val isVisible: Boolean,
    val onVisibleChange: () -> Unit,
)

@Composable
private fun PasswordField.Prefix() {
    return Icon(Icons.Default.Lock, contentDescription = "password_field_prefix")
}

@Composable
private fun PasswordField.Suffix() {
    return Icon(
        if (this.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
        modifier = Modifier.clickable {
            this.onVisibleChange()
        },
        contentDescription = "password_field_visibility"
    )
}

@Composable
private fun rememberPasswordVisibility(): PasswordField {
    var isVisible by remember { mutableStateOf(false) }
    return PasswordField(
        isVisible = isVisible,
        onVisibleChange = {
            isVisible = !isVisible
        }
    )
}

@Preview
@Composable
private fun AuthPagePreview() {
    val mockCallback = object : AuthViewModelCallback {
        override fun signIn() {}
        override fun signUp() {}
        override fun onContentTypeChange(authContentType: AuthContentType) {}
    }
    AuthPage(
        signInForm = FormBuilder<SignInForm>().apply {
            addField(
                SignInForm::username, FieldProperties(
                    validator = EmailValidator(),
                    isRequired = true,
                )
            )
        },
        signUpForm = FormBuilder(),
        authCallback = mockCallback
    )
}

@Preview
@Composable
private fun AuthPagePreviewSignUp() {
    val mockCallback = object : AuthViewModelCallback {
        override fun signIn() {}
        override fun signUp() {}
        override fun onContentTypeChange(authContentType: AuthContentType) {}
    }
    AuthPage(
        signInForm = FormBuilder<SignInForm>().apply {
            addField(
                SignInForm::username, FieldProperties(
                    validator = EmailValidator(),
                    isRequired = true,
                )
            )
        },
        uiState = AuthenticationUiState(contentType = AuthContentType.SIGN_UP),
        signUpForm = FormBuilder(),
        authCallback = mockCallback
    )
}
