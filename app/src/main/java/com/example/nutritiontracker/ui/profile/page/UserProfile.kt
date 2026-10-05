package com.example.nutritiontracker.ui.profile.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.BodyMassIndexStatus
import com.example.nutritiontracker.domain.Gender
import com.example.nutritiontracker.domain.PersonalActivities
import com.example.nutritiontracker.domain.createDefaultUserProfile
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
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.withStyle


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

object PersonalActivitiesUtility {
    @Composable
    fun String.toPersonalActivities(): PersonalActivities {
        return when (this) {
            "LIGHT" -> PersonalActivities.LIGHT
            "SEDENTARY" -> PersonalActivities.SEDENTARY
            "MODERATE" -> PersonalActivities.MODERATE
            "ACTIVE" -> PersonalActivities.ACTIVE
            else -> PersonalActivities.LIGHT
        }
    }

    @Composable
    fun PersonalActivities.label(): Pair<String, String> {
        val labels =
            stringArrayResource(R.array.personal_daily_activities).mapIndexed { index, value ->
                Pair(value, stringArrayResource(R.array.personal_daily_activities_sub)[index])
            }

        return when (this) {
            PersonalActivities.LIGHT -> Pair(labels[0].first, labels[0].second)
            PersonalActivities.SEDENTARY -> Pair(labels[1].first, labels[1].second)
            PersonalActivities.MODERATE -> Pair(labels[2].first, labels[2].second)
            PersonalActivities.ACTIVE -> Pair(labels[3].first, labels[3].second)
        }
    }

    //TODO: IMPORT SVG
    fun PersonalActivities.icon(): ImageVector {
        return Icons.Default.Close
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfilePage() {
    Scaffold { innerPadding ->
        UserProfileContent(modifier = Modifier.padding(innerPadding))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun UserProfilePagePreview() {
    NutritionTrackerTheme {
        UserProfilePage()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileContent(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Form(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = Spacing.m),
        verticalSpacing = Spacing.m
    ) { isValid ->
        DemographicSection()
        MeasurementSection()
        DailyActivitiesSection()
        Button(enabled = isValid, onClick = {}) {
            Text("Test")
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DemographicSection() {
    UserProfileContentSection(
        label = "1. INFORMASI DEMOGRAFIS",
        icon = Icons.Default.Info,
    ) {
        DemographicContent()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DemographicContent() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        Text("Jenis Kelamin", style = typography.labelLarge.copy(fontWeight = FontWeight.W500))
        RowOptionForm(
            fieldProperties = FieldProperties(
                validator = OptionFieldValidator(),
                isRequired = true
            ),
            fieldName = "Gender Options",
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            items = Gender.entries.map { it.label() },
            itemWeight = 1f
        ) { gender, genderState ->
            Surface(
                color = if (gender == genderState) colorScheme.primaryContainer else colorScheme.tertiary,
                shape = RoundedCornerShape(Spacing.m)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.xl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    gender.toGender().icon()?.let { icon ->
                        Icon(icon, contentDescription = "option_${gender.toGender().label()}")
                    }
                    Text(gender.toGender().label())
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
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
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            NumberPickerField(
                modifier = Modifier.weight(1f),
                fieldProperties = FieldProperties(
                    validator = MockNumberValidator(),
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
                ),
                initialValue = 0,
                fieldName = "Age",
                label = "AGE"
            )
        }
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(Spacing.s)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.s),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
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
        verticalArrangement = Arrangement.spacedBy(Spacing.s),
        items = PersonalActivities.entries.map { it.name }
    ) { activity, activityState ->
        Surface(shape = RoundedCornerShape(Spacing.s)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.m)
            ) {
                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceVariant,
                    contentColor = colorScheme.inverseOnSurface
                ) {
                    Icon(
                        modifier = Modifier.padding(Spacing.xxs),
                        imageVector = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().icon()
                        },
                        contentDescription = "${activity}_icon"
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = with(PersonalActivitiesUtility) {
                        activity.toPersonalActivities().label().first
                    })
                    Text(
                        text = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().label().second
                        },
                        style = typography.labelSmall
                    )

                }

                Checkbox(checked = activity == activityState, onCheckedChange = null)
            }
        }

    }
}


@Composable
private fun DailyEstimationSection() {
    UserProfileContentSection {
        DailyEstimationContent()
    }
}

@Composable
private fun DailyEstimationContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        DailyEstimationContentHeader()
        DailyEstimationContentBody()
    }
}

@Composable
private fun DailyEstimationContentHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        Surface(shape = CircleShape) {
            Icon(
                Icons.Default.LocalFireDepartment,
                modifier = Modifier.padding(Spacing.s),
                contentDescription = "daily_estimation"
            )
        }
        Column {
            Text(stringResource(R.string.user_profile_daily_estimation_section_title))
            Text(text = buildAnnotatedString {
                withStyle(
                    style = typography.titleMedium,
                    color = LocalContentColor.current
                ) {
                    append("~${createDefaultUserProfile().nutritionNeeds}\t")
                }
                withStyle(
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                ) {
                    append("kkal / day")
                }
            })
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Chip(
                color = colorScheme.primaryContainer,
                contentColor = colorScheme.onPrimaryContainer
            ) {
                Text("TDEE Siap", style = typography.labelLarge)
            }
            Text("Defisit: -350 kkal", style = typography.titleSmall)
        }
    }
}

@Composable
private fun DailyEstimationContentBody() {
    Button(
        shape = RoundedCornerShape(Spacing.s),
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Calculate, contentDescription = "calculate_nutrients_need")
        Text("Simpan & Hitung Kebutuhan Nutrisi")
    }
}


@Preview
@Composable
private fun DailyActivitiesContentPreview() {
    NutritionTrackerTheme {
        DailyEstimationSection()
    }
}




