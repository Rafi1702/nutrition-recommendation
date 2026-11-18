package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSelectionConfirmation(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if (LocalInspectionMode.current) {
        Surface(
            modifier = Modifier.background(colorScheme.primaryContainer),
            shape = RoundedCornerShape(28.dp)
        ) {
            ConfirmationDialogContent()
        }
    } else {
        BasicAlertDialog(
            onDismissRequest = { onDismissRequest() },
            modifier = Modifier
                .background(colorScheme.primaryContainer),
            properties = DialogProperties(
                windowTitle = "Test Only"
            )
        ) {
            ConfirmationDialogContent()
        }
    }
}

@Composable
fun ConfirmationDialogContent() {
    Row(Modifier.fillMaxWidth()) {
        Button(onClick = {}) { Text("Yes") }
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodSelectionConfirmationPreview() {
    NutritionTrackerTheme {
        FoodSelectionConfirmation { }
    }
}