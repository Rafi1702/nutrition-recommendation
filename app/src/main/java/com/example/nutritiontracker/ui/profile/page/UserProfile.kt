package com.example.nutritiontracker.ui.profile.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.BodyMassIndexStatus
import com.example.nutritiontracker.domain.Gender
import com.example.nutritiontracker.domain.PersonalActivities
import com.example.nutritiontracker.ui.components.CannotEmptyValidator
import com.example.nutritiontracker.ui.components.Chip
import com.example.nutritiontracker.ui.components.ColumnOptionForm
import com.example.nutritiontracker.ui.components.DatePickerField
import com.example.nutritiontracker.ui.components.FieldProperties
import com.example.nutritiontracker.ui.components.Form
import com.example.nutritiontracker.ui.components.MockNumberValidator
import com.example.nutritiontracker.ui.components.OptionFieldValidator
import com.example.nutritiontracker.ui.components.RowOptionForm
import com.example.nutritiontracker.ui.components.textfield.NumberPickerField
import com.example.nutritiontracker.ui.profile.components.UserProfileContentSection
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography

//enum class Gender(val label: String, val imageVector: ImageVector? = null) {
//    MALE("Male", Icons.Default.Place),
//    FEMALE("Female", Icons.Default.Place);
//}
//
//private fun String.toGender() = when (this) {
//    "Male" -> Gender.MALE
//    "Female" -> Gender.FEMALE
//    else -> Gender.MALE
//}

@Composable
fun String.toGender(): Gender {
    return when (this) {
        "Male" -> Gender.MALE
        "Female" -> Gender.FEMALE
        else -> Gender.MALE
    }
}

@Composable
fun Gender.label(): String {
    val gendersResource = stringArrayResource(R.array.personal_gender)

    return when (this) {
        Gender.MALE -> gendersResource[0]
        Gender.FEMALE -> gendersResource[1]
    }
}

@Composable
fun Gender.icon(): ImageVector? {
    return when (this) {
        Gender.MALE -> Icons.Default.Male
        Gender.FEMALE -> Icons.Default.Female
    }
}

@Composable
fun BodyMassIndexStatus.statusColor(): Pair<Color, Color> {
    return when (this) {
        BodyMassIndexStatus.NORMAL -> (colorScheme.primaryContainer to colorScheme.onPrimaryContainer)
        else -> (colorScheme.errorContainer to colorScheme.onErrorContainer)
    }
}

@Composable
fun BodyMassIndexStatus.label(): String {
    val bmiLevelsResource = stringArrayResource(R.array.body_mass_index_level)

    return when (this) {
        BodyMassIndexStatus.NORMAL -> bmiLevelsResource[1]
        BodyMassIndexStatus.BELOW_AVERAGE -> bmiLevelsResource[0]
        else -> ""
    }
}

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
    val scrollState = rememberScrollState()
    Form(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        verticalSpacing = 16.dp
    ) { isValid ->
        DemographicSection()
        MeasurementSection()
        DailyActivitiesSection()
        Button(enabled = isValid, onClick = {}) {
            Text("Test")
        }

    }
}


@Composable
fun DemographicSection() {
    UserProfileContentSection(
        label = "1. INFORMASI DEMOGRAFIS",
        icon = Icons.Default.Info,
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
            items = Gender.entries.map { it.label() },
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
                    gender.toGender().icon()?.let { icon ->
                        Icon(icon, contentDescription = "option_${gender.toGender().label()}")
                    }
                    Text(gender.toGender().label())
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
            DatePickerField(
                fieldName = "birth", fieldProperties = FieldProperties(
                    validator = CannotEmptyValidator(),
                    isRequired = true
                )
            )
        }
    }
}

@Composable
private fun MeasurementSection() {
    UserProfileContentSection(
        label = "2. PENGUKURAN",
        icon = Icons.Default.Info,
    ) {
        MeasurementContent()
    }
}

@Composable
private fun MeasurementContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NumberPickerField(
                modifier = Modifier.weight(1f),
                fieldProperties = FieldProperties(
                    validator = MockNumberValidator(),
//                    isRequired = true
                ),
                initialValue = 0,
                fieldName = "Age",
                label = "HEIGHT",
                unit = "CM"
            )
            NumberPickerField(
                modifier = Modifier.weight(1f),
                fieldProperties = FieldProperties(
                    validator = MockNumberValidator(),
//                    isRequired = true
                ),
                initialValue = 0,
                fieldName = "Age",
                label = "AGE"
            )
        }
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Speed, contentDescription = "accelerate_icon")
                Text(stringResource(R.string.body_mass_index), style = typography.labelLarge)
                Spacer(modifier = Modifier.weight(1f))
                Text("21.5", style = typography.titleMedium)
                Chip(
                    color = BodyMassIndexStatus.NORMAL.statusColor().first,
                    contentColor = BodyMassIndexStatus.NORMAL.statusColor().second,
                ) {
                    Text(BodyMassIndexStatus.NORMAL.label())
                }
            }
        }
    }
}

@Composable
private fun DailyActivitiesSection() {
    UserProfileContentSection(label = "AKTIVITAS HARIAN", modifier = Modifier.fillMaxWidth()) {
        DailyActivitiesContent()
    }
}

@Composable
private fun DailyActivitiesContent() {
    ColumnOptionForm(
        fieldName = "daily_activities",
        fieldProperties = FieldProperties(
            validator = OptionFieldValidator(),
            isRequired = true
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        items = PersonalActivities.entries.map { it.name }
    ) { activity, activityState ->
        Surface(shape = RoundedCornerShape(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(modifier = Modifier.weight(1f), text = activity)
                Checkbox(checked = activity == activityState, onCheckedChange = null)
            }
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
                    label = "AGE"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CardFormSectionPreview() {

}

