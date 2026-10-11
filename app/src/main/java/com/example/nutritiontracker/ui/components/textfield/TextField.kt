package com.example.nutritiontracker.ui.components.textfield


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.FieldRegister
import com.example.nutritiontracker.ui.components.PasswordValidator
import com.example.nutritiontracker.ui.components.currentForm
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle
import kotlin.reflect.KProperty1
import androidx.compose.material3.TextField as M3TextField

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
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
    isError: Boolean? = null,
    isRequired: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
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

    val prefixContent: @Composable (() -> Unit)? = prefix?.let {
        {
            Box(modifier = Modifier.padding(end = Spacing.s)){
                prefix.invoke()
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Text(text = label, style = typography.labelMedium)
            if (isRequired) Text(
                text = "*",
                style = typography.labelMedium.copy(color = colorScheme.error)
            )
        }
        when {
            isSecure -> {
                SecureTextField(
                    state = state,
                    modifier = modifier.onKeyEvent(ignoredKeyEvent),
                    trailingIcon = trailingIcon,
                    isError = hasError,
                    supportingText = supportingTextContent,
                    keyboardOptions = keyboardOptions,
                    suffix = suffix,
                    prefix = prefixContent,
                    placeholder = placeholder,
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
                    trailingIcon = trailingIcon,
                    isError = hasError,
                    placeholder = placeholder,
                    supportingText = supportingTextContent,
                    suffix = suffix,
                    prefix = prefixContent,
                    shape = RoundedCornerShape(cornerRadius),
                    colors = textFieldColors,
                    textStyle = textStyle
                )
            }
        }
    }
}

@Composable
fun <T> TextFormField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    properties: FieldProperties<T>,
    label: String = "Label",
    cornerRadius: Dp = Spacing.s,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    isSecure: Boolean? = null,
    isRequired: Boolean = false,
    keyboardOption: KeyboardOptions = KeyboardOptions(),
    style: TextStyle = LocalTextStyle.current,
) {
    val (_, errorMessage) = properties
    val resolvedIsSecure = isSecure ?: properties.validator.any { it is PasswordValidator }

    TextField(
        state = state,
        modifier = modifier,
        isRequired = isRequired,
        label = label,
        errorMessage = errorMessage.value,
        isSecure = resolvedIsSecure,
        placeholder = placeholder,
        cornerRadius = cornerRadius,
        prefix = prefix,
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
    cornerRadius: Dp = Spacing.s,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
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
fun <T : Any> TextFormField(
    modifier: Modifier = Modifier,
    fieldName: KProperty1<T, *>,
    fieldProperties: FieldProperties<CharSequence>? = null,
    label: String = "Label",
    cornerRadius: Dp = Spacing.s,
    borderColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isSecure: Boolean? = null,
) {

    val formBuilder = currentForm<T>()

    val existingField = formBuilder.getField<CharSequence>(fieldName)
    val initialText = existingField?.valueState?.value?.toString() ?: ""

    val state = rememberTextFieldState(initialText)

    val boundProps = remember(fieldName) {
        val base = existingField ?: fieldProperties ?: FieldProperties(validator = emptyList())
        base.copy(valueState = derivedStateOf { state.text })
    }

    FieldRegister(
        fieldProperties = boundProps,
        name = fieldName,
    ) {
        val currentProps = formBuilder.getField(fieldName) ?: boundProps
        TextFormField(
            state = state,
            properties = currentProps,
            modifier = modifier,
            cornerRadius = cornerRadius,
            label = label,
            prefix = prefix,
            borderColor = borderColor,
            isRequired = boundProps.isRequired,
            placeholder = placeholder,
            backgroundColor = backgroundColor,
            suffix = suffix,
            trailingIcon = trailingIcon,
            isSecure = isSecure
        )
    }
}
