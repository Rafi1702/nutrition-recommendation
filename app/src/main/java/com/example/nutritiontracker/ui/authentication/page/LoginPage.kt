package com.example.nutritiontracker.ui.authentication.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.EmailValidator
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.PasswordValidator
import com.example.nutritiontracker.ui.components.TextFormField
import com.example.nutritiontracker.ui.theme.typography

@Preview(showBackground = true)
@Composable
internal fun LoginPage(modifier: Modifier = Modifier) {
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
        Text("Sign in LOGO", style = typography.displaySmall)
        Surface(shadowElevation = 8.dp, shape = MaterialTheme.shapes.medium) {
            Form(modifier = Modifier.padding(16.dp), verticalSpacing = 16.dp) { isValid, form ->
                TextFormField(
                    modifier = Modifier.fillMaxWidth(),
                    fieldProperties = ("email" to FieldProperties(
                        validator = EmailValidator(),
                        isRequired = true
                    )),
                    formBuilder = form,
                    label = "Email",
                )

                TextFormField(
                    modifier = Modifier.fillMaxWidth(),
                    fieldProperties = ("password" to FieldProperties(
                        validator = PasswordValidator(),
                        isRequired = true,
                    )),
                    formBuilder = form,
                    label = "Password",
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start), verticalAlignment = Alignment.CenterVertically){
                    Checkbox(
                        checked = true,
                        onCheckedChange = {},
                        enabled = true,
                    )
                    Text("Remember me")
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
}
