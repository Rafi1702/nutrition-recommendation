package com.example.nutritiontracker.ui.authentication.page

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
import androidx.compose.material3.MaterialTheme
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
import com.example.nutritiontracker.ui.theme.typography

private enum class AuthContentType {
    SIGN_IN,
    SIGN_UP
}

@Preview(showBackground = true)
@Composable
internal fun LoginPage(modifier: Modifier = Modifier) {
    var authContent by remember { mutableStateOf(AuthContentType.SIGN_UP) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .sizeIn(
                maxWidth = 480.dp,
                maxHeight = 200.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterVertically)
    ) {
        when (authContent) {
            AuthContentType.SIGN_IN -> {
                Text("Sign in LOGO", style = typography.displaySmall)
                SignInContent()
            }

            AuthContentType.SIGN_UP -> {
                SignUpContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContent() {
    Surface(shadowElevation = 8.dp, shape = MaterialTheme.shapes.medium) {
        Form(modifier = Modifier.padding(16.dp), verticalSpacing = 16.dp) { isValid, form ->
            TextFormField(
                formBuilder = form,
                label = "Password",
                fieldName = "password_sign_up",
                initialValue = "TESST",
                fieldProperties = FieldProperties(
                    validator = PasswordValidator(),
                    isRequired = true,
                ),
            )
            TextFormField(
                formBuilder = form,
                fieldName = "password_sign_up_confirm",
                label = "Confirm password",
                initialValue = "Ts",
                fieldProperties = FieldProperties(
                    validator = MatchValidator(
                        form = form,
                        targetFieldKey = "password_sign_up"
                    ),
                    isRequired = true
                ),

                )
        }
    }
}

@Composable
private fun SignInContent() {
    Surface(shadowElevation = 8.dp, shape = MaterialTheme.shapes.medium) {
        Form(modifier = Modifier.padding(16.dp), verticalSpacing = 16.dp) { isValid, form ->
            TextFormField(
                modifier = Modifier.fillMaxWidth(),
                fieldProperties = FieldProperties(
                    validator = EmailValidator(),
                    isRequired = true
                ),
                fieldName = "email",
                formBuilder = form,
                label = "Email",
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
                    onCheckedChange = { check -> },
                    removePadding = true,
                )
                Text("Remember me", style = typography.bodyMedium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Doesn't have an account?")
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
                        onClick = { /* TODO: Navigate to Sign Up */ }) {
                        Text("Sign Up")
                    }
                }
            }
            Button(
                onClick = { /* TODO: Handle login */ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                enabled = isValid
            ) {
                Text("Login", style = typography.labelLarge)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}
