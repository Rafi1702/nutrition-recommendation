package com.example.nutritiontracker.ui.components.textfield


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FieldRegister
import com.example.nutritiontracker.ui.components.PasswordValidator
import com.example.nutritiontracker.ui.theme.LocalForm
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import androidx.compose.material3.TextField as M3TextField

@Preview(name = "TextField", showBackground = true)
@Composable
fun CustomOutlinedTextFieldPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
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
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String = "Label",
    cornerRadius: Dp = Spacing.s,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    isSecure: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    decorationBox: @Composable ((@Composable (() -> Unit)) -> Unit)? = null,

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

    when {
        decorationBox != null -> {
            BasicTextField(
                modifier = modifier,
                value = value,
                onValueChange = onValueChange,
                textStyle = textStyle,
                decorationBox = decorationBox,
                minLines = minLines,
                maxLines = maxLines
            )
        }

        else -> {
            M3TextField(
                value = value,
                readOnly = readOnly,
                onValueChange = onValueChange,
                enabled = enabled,
                modifier = modifier,
                label = { Text(text = label) },
                trailingIcon = trailingIcon,
                suffix = suffix,
                minLines = minLines,
                maxLines = maxLines,
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
    }
}

@Composable
fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: String = "Label",
    cornerRadius: Dp = Spacing.s,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    isSecure: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
    isError: Boolean? = null,
    keyboardOptions: KeyboardOptions,
    decorationBox: TextFieldDecorator? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {

    val resolvedBorderColor =
        if (borderColor == Color.Unspecified) colorScheme.onSurfaceVariant else borderColor
    val resolvedBackgroundColor =
        if (backgroundColor == Color.Unspecified) colorScheme.surfaceVariant else backgroundColor

    val textFieldColors = TextFieldDefaults.colors(
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

    val labelContent: (@Composable TextFieldLabelScope.() -> Unit)? = if (label.isNotBlank()) {
        { Text(text = label) }
    } else {
        null
    }

    val supportingTextContent: (@Composable () -> Unit)? =
        if (errorMessage != null && isError == true) {
            { Text(text = errorMessage) }
        } else {
            null
        }

    val hasError = errorMessage != null && isError == true

    val ignoredKeyEvent: (keyEvent: KeyEvent) -> Boolean = remember {
        { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                keyEvent.key == Key.Spacebar || keyEvent.key == Key.Tab
            } else {
                false
            }
        }
    }

    when {
        isSecure -> {
            SecureTextField(
                state = state,
                modifier = modifier.onKeyEvent(ignoredKeyEvent),
                label = labelContent,
                trailingIcon = trailingIcon,
                isError = hasError,
                supportingText = supportingTextContent,
                keyboardOptions = keyboardOptions,
                suffix = suffix,
                textStyle = textStyle,
                shape = RoundedCornerShape(cornerRadius),
                colors = textFieldColors,
            )
        }

        decorationBox != null -> {
            BasicTextField(
                state = state,
                modifier = modifier,
                decorator = decorationBox,
                textStyle = textStyle
            )
        }

        else -> {
            M3TextField(
                state = state,
                modifier = modifier.onPreviewKeyEvent(ignoredKeyEvent),
                label = labelContent,
                trailingIcon = trailingIcon,
                isError = hasError,
                supportingText = supportingTextContent,
                suffix = suffix,
                shape = RoundedCornerShape(cornerRadius),
                colors = textFieldColors,
                textStyle = textStyle
            )
        }
    }
}

@Composable
fun <T> TextFormField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    properties: FieldProperties<T>,
    label: String = "Label",
    cornerRadius: Dp = 8.dp,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isSecure: Boolean? = null,
    keyboardOption: KeyboardOptions = KeyboardOptions(),
    style: TextStyle = LocalTextStyle.current,
) {
    val (_, errorMessage) = properties
    val resolvedIsSecure = isSecure ?: properties.validator.any { it is PasswordValidator }


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
        isError = properties.isDirty.value,
        keyboardOptions = keyboardOption,
    )
}

@Composable
fun TextFormField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = { },
    label: String = "Label",
    cornerRadius: Dp = 8.dp,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isSecure: Boolean? = null,
    keyboardOption: KeyboardOptions = KeyboardOptions(),
    textStyle: TextStyle = LocalTextStyle.current,
    decorationBox: @Composable ((@Composable (() -> Unit)) -> Unit)? = null,
    minLines: Int = 1,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    enabled: Boolean = true,
) {


    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        isSecure = false,
        cornerRadius = cornerRadius,
        borderColor = borderColor,
        backgroundColor = backgroundColor,
        suffix = suffix,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOption,
        textStyle = textStyle,
        enabled = enabled,
        maxLines = maxLines,
        minLines = minLines,
        readOnly = readOnly,
        decorationBox = decorationBox
    )
}

@Composable
fun TextFormField(
    modifier: Modifier = Modifier,
    fieldName: String,
    fieldProperties: FieldProperties<CharSequence>,
    initialValue: String = "",
    label: String = "Label",
    cornerRadius: Dp = Spacing.s,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isSecure: Boolean? = null,
) {
    val state = rememberTextFieldState(initialValue)
    LaunchedEffect(fieldProperties.isValid,) {
        Log.d("[BASIC_TEXT_FIELD]", "field valid: ${fieldProperties.isValid}")
    }
    FieldRegister(
        fieldProperties = fieldProperties.copy(valueState = remember(state) { derivedStateOf { state.text } }),
        name = fieldName
    ) {
        TextFormField(
            state = state,
            properties = LocalForm.current.getField(fieldName) ?: fieldProperties,
            modifier = modifier,
            label = label,
            cornerRadius = cornerRadius,
            borderColor = borderColor,
            backgroundColor = backgroundColor,
            suffix = suffix,
            trailingIcon = trailingIcon,
            isSecure = isSecure
        )
    }
}
