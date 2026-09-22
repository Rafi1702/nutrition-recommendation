package com.example.nutritiontracker.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.colorScheme
import androidx.compose.material3.TextField as M3TextField

@Preview(name = "TextField", showBackground = true)
@Composable
fun CustomOutlinedTextFieldPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(value = "", label = "Email")
        TextField(
            value = "",
            label = "Password",
            isSecure = true,
            suffix = {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = "Visibility",
                )
            })
    }
}

@Composable
fun TextField(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    label: String = "Label",
    cornerRadius: Dp = 8.dp,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    isSecure: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
) {
    val resolvedBorderColor = if (borderColor == Color.Unspecified) {
        colorScheme.onSurfaceVariant
    } else {
        borderColor
    }
    val resolvedBackgroundColor = if (backgroundColor == Color.Unspecified) {
        colorScheme.surfaceVariant
    } else {
        backgroundColor
    }

    M3TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(text = label) },
        trailingIcon = trailingIcon,
        suffix = suffix,
        shape = RoundedCornerShape(cornerRadius),
        visualTransformation = if (isSecure) {
            remember { PasswordVisualTransformation() }
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isSecure) KeyboardType.Password else KeyboardType.Text
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,


            focusedContainerColor = resolvedBackgroundColor,
            unfocusedContainerColor = resolvedBackgroundColor,
            disabledContainerColor = resolvedBackgroundColor,
            errorContainerColor = resolvedBackgroundColor,

            focusedLabelColor = resolvedBorderColor,
            unfocusedLabelColor = resolvedBorderColor
        )
    )
}

@Composable
fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: String = "Label",
    cornerRadius: Dp = 8.dp,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    isSecure: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
    isError: Boolean? = null,
) {

    val resolvedBorderColor = if (borderColor == Color.Unspecified) {
        colorScheme.onSurfaceVariant
    } else {
        borderColor
    }
    val resolvedBackgroundColor = if (backgroundColor == Color.Unspecified) {
        colorScheme.surfaceVariant
    } else {
        backgroundColor
    }

    if (isSecure) {
        return SecureTextField(
            state = state,
            modifier = modifier,
            label = { Text(text = label) },
            trailingIcon = trailingIcon,
            isError = errorMessage != null && isError == true,
            supportingText = if (errorMessage != null && isError == true) {
                { Text(text = errorMessage) }
            } else {
                null
            },
            suffix = suffix,
            shape = RoundedCornerShape(cornerRadius),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,

                focusedContainerColor = resolvedBackgroundColor,
                unfocusedContainerColor = resolvedBackgroundColor,
                disabledContainerColor = resolvedBackgroundColor,
                errorContainerColor = resolvedBackgroundColor,

                focusedLabelColor = resolvedBorderColor,
                unfocusedLabelColor = resolvedBorderColor
            ),
        )
    }

    M3TextField(
        state = state,
        modifier = modifier,
        label = { Text(text = label) },
        trailingIcon = trailingIcon,
        isError = errorMessage != null && isError == true,
        supportingText = if (errorMessage != null && isError == true) {
            { Text(text = errorMessage) }
        } else {
            null
        },
        suffix = suffix,
        shape = RoundedCornerShape(cornerRadius),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,

            focusedContainerColor = resolvedBackgroundColor,
            unfocusedContainerColor = resolvedBackgroundColor,
            disabledContainerColor = resolvedBackgroundColor,
            errorContainerColor = resolvedBackgroundColor,

            focusedLabelColor = resolvedBorderColor,
            unfocusedLabelColor = resolvedBorderColor
        )
    )
}

@Composable
fun TextFormField(
    state: TextFieldState,
    properties: FieldProperties<CharSequence>,
    modifier: Modifier = Modifier,
    label: String = "Label",
    cornerRadius: Dp = 8.dp,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isError: Boolean? = null,
    isSecure: Boolean? = null,
) {
    val (validator, errorMessage) = properties
    val resolvedIsSecure =
        isSecure ?: (validator is PasswordValidator || validator is MatchValidator)

    TextField(
        state = state,
        modifier = modifier,
        label = label,
        errorMessage = errorMessage.value,
        isSecure = resolvedIsSecure,
        cornerRadius = cornerRadius,
        borderColor = borderColor,
        backgroundColor = backgroundColor,
        suffix = suffix,
        trailingIcon = trailingIcon,
        isError = isError
    )
}

@Composable
fun TextFormField(
    modifier: Modifier = Modifier,
    fieldName: String,
    fieldProperties: FieldProperties<CharSequence>,
    formBuilder: FormBuilder,
    initialValue: String = "",
    label: String = "Label",
    cornerRadius: Dp = 8.dp,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isSecure: Boolean? = null,
    effectKey: Any? = Unit,
) {
    val state = rememberTextFieldState(initialValue)

    val boundProperties = remember(fieldProperties, state) {
        fieldProperties.copy(
            valueProvider = { state.text }
        )
    }
    RegisterFormListener(
        form = formBuilder,
        name = fieldName,
        fieldProperties = boundProperties,
        effectKey = effectKey
    )

    DisposableEffect(fieldName) {
        onDispose { formBuilder.removeField(fieldName) }
    }

    TextFormField(
        state = state,
        properties = formBuilder.getField(fieldName) ?: boundProperties,
        modifier = modifier,
        label = label,
        cornerRadius = cornerRadius,
        borderColor = borderColor,
        backgroundColor = backgroundColor,
        suffix = suffix,
        trailingIcon = trailingIcon,
        isError = formBuilder.fieldRegistry[fieldName]?.isDirty?.value,
        isSecure = isSecure
    )
}