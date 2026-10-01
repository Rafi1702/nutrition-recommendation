package com.example.nutritiontracker.ui.profile.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.DatePickerField
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.MockNumberValidator
import com.example.nutritiontracker.ui.components.OptionFieldValidator
import com.example.nutritiontracker.ui.components.RowOptionForm
import com.example.nutritiontracker.ui.components.textfield.NumberPickerField
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

enum class Gender(val label: String, val imageVector: ImageVector? = null) {
    MALE("Male", Icons.Default.Place),
    FEMALE("Female", Icons.Default.Place);
}

private fun String.toGender() = when (this) {
    "Male" -> Gender.MALE
    "Female" -> Gender.FEMALE
    else -> Gender.MALE
}

data class CardFormHeaderProps(
    val title: String? = null,
    val icon: ImageVector? = null
)

@Preview()
@Composable
fun UserProfilePage() {
    Scaffold { innerPadding ->
        UserProfileContent(modifier = Modifier.padding(innerPadding))
    }
}


@Preview(showBackground = true)
@Composable
fun UserProfileContent(modifier: Modifier = Modifier) {
    Form(
        modifier = modifier,
        verticalSpacing = 16.dp
    ) {
        DemographicSection()
        MeasurementSection()
    }
}


@Composable
fun DemographicSection() {
    CardFormSection(
        contentPadding = PaddingValues(horizontal = 16.dp),
        cardHeaderProps = CardFormHeaderProps(
            icon = Icons.Default.Info,
            title = "1. INFORMASI DEMOGRAFIS"
        )
    ) {
        DemographicContent()
    }
}


@Composable
fun DemographicContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Jenis Kelamin", style = typography.labelLarge.copy(fontWeight = FontWeight.W500))
        RowOptionForm(
            fieldProperties = FieldProperties(
                validator = OptionFieldValidator(),
                isRequired = true
            ),
            fieldName = "Gender Options",
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            items = Gender.entries.map { it.label },
            itemWeight = 1f
        ) { gender, genderState ->
            Surface(
                color = if (gender == genderState) colorScheme.primaryContainer else colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    gender.toGender().imageVector?.let { icon ->
                        Icon(icon, contentDescription = "option_${gender.toGender().label}")
                    }
                    Text(gender.toGender().label)
                }
            }
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Placeholder")
                Chip(label = "Umur")
            }
            DatePickerField()
        }
    }
}

@Composable
private fun MeasurementSection() {
    CardFormSection(
        contentPadding = PaddingValues(horizontal = 16.dp),
        cardHeaderProps = CardFormHeaderProps(
            icon = Icons.Default.Info,
            title = "2. PENGUKURAN"
        )
    ) {
        MeasurementContent()
    }
}

@Composable
private fun MeasurementContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            NumberPickerField(
                fieldProperties = FieldProperties(
                    validator = MockNumberValidator(),
                    isRequired = true
                ),
                initialValue = 0,
                fieldName = "Age",
                leadingIcon = {

                }
            )
        }
    }
}

@Preview
@Composable
private fun MeasurementContentPreview() {
    NutritionTrackerTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                NumberPickerField(
                    fieldProperties = FieldProperties(
                        validator = MockNumberValidator(),
                        isRequired = true
                    ),
                    initialValue = 0,
                    fieldName = "Age",
                )
            }
        }
    }
}



@Composable
private fun CardFormSection(
    cardHeaderProps: CardFormHeaderProps = CardFormHeaderProps(),
    contentPadding: PaddingValues = PaddingValues.Zero,
    content: @Composable (() -> Unit)? = null
) {
    val (title, icon) = cardHeaderProps
    Card {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            title?.let {
                CardFormHeader(
                    title = title,
                    icon = icon,
                    modifier = Modifier.padding(contentPadding)
                )
            }
            Box(modifier = Modifier.padding(contentPadding)) {
                content?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardFormSectionPreview() {

}


@Composable
private fun CardFormHeader(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(icon, contentDescription = null)
        }
        Text(title, style = typography.titleMedium)
    }
}

@Preview(name = "CardFormHeader", showBackground = true)
@Composable
private fun CardFormHeaderPreview() {
    CardFormHeader(title = "Placeholder", icon = Icons.Default.Info)
}
