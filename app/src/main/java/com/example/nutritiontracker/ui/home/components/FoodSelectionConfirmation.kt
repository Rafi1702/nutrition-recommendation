package com.example.nutritiontracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

interface DialogEvent {
    fun onDismissRequest()
    fun onConfirm()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSelectionConfirmation(
    event: DialogEvent,
) {
    if (LocalInspectionMode.current) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = colorScheme.surface
        ) {
            ConfirmationDialogContent(
                onDismissRequest = { event.onDismissRequest() },
                onConfirm = { event.onConfirm() }
            )
        }
    } else {
        BasicAlertDialog(
            onDismissRequest = { event.onDismissRequest() },
            properties = DialogProperties(
                windowTitle = "Food Selection Confirmation"
            )
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = colorScheme.surface
            ) {
                ConfirmationDialogContent(
                    onDismissRequest = { event.onDismissRequest() },
                    onConfirm = { event.onConfirm() }
                )
            }
        }
    }
}

@Composable
fun ConfirmationDialogContent(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(Spacing.l),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = "Confirm Selection",
                style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.onSurface
            )
            Text(
                text = "Are you sure you want to proceed with this selection?",
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s, Alignment.End)
        ) {
            OutlinedButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorScheme.onSurfaceVariant
                )
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError
                )
            ) {
                Text("Confirm")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodSelectionConfirmationPreview() {
    NutritionTrackerTheme {
        FoodSelectionConfirmation(object: DialogEvent{
            override fun onDismissRequest() {
                TODO("Not yet implemented")
            }

            override fun onConfirm() {
                TODO("Not yet implemented")
            }

        })
    }
}