package com.example.nutritiontracker.ui.profile.page

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.BodyMassIndexStatus
import com.example.nutritiontracker.domain.model.Gender
import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.domain.model.createDefaultUserProfile
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
        "Male", "Pria" -> Gender.MALE
        "Female", "Wanita" -> Gender.FEMALE
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
        BodyMassIndexStatus.NORMAL -> (colorScheme.primary to colorScheme.onPrimary)
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
            "SEDENTARY" -> PersonalActivities.SEDENTARY
            "LIGHT" -> PersonalActivities.LIGHT
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
            PersonalActivities.SEDENTARY -> Pair(labels[0].first, labels[0].second)
            PersonalActivities.LIGHT -> Pair(labels[1].first, labels[1].second)
            PersonalActivities.MODERATE -> Pair(labels[2].first, labels[2].second)
            PersonalActivities.ACTIVE -> Pair(labels[3].first, labels[3].second)
        }
    }

    fun PersonalActivities.icon(): ImageVector {
        return when (this) {
            PersonalActivities.SEDENTARY -> Icons.Default.Weekend
            PersonalActivities.LIGHT -> Icons.AutoMirrored.Filled.DirectionsWalk
            PersonalActivities.MODERATE -> Icons.Default.FitnessCenter
            PersonalActivities.ACTIVE -> Icons.AutoMirrored.Filled.DirectionsRun
        }
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
        DailyEstimationSection()
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
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
        Text(
            "Jenis Kelamin Biologis",
            style = typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
        )
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
            val isSelected = gender == genderState
            Surface(
                color = if (isSelected) colorScheme.primary else colorScheme.surfaceVariant,
                contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface,
                shape = RoundedCornerShape(Spacing.s)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.m, horizontal = Spacing.s),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    gender.toGender().icon()?.let { icon ->
                        Icon(
                            icon,
                            contentDescription = "option_${gender.toGender().label()}",
                            tint = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                        )
                    }
                    Text(
                        gender.toGender().label(),
                        style = typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                        )
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tanggal Lahir",
                    style = typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                )
                Chip(
                    color = colorScheme.primaryContainer,
                    contentColor = colorScheme.onPrimaryContainer
                ) {
                    Text("26 Tahun", style = typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                }
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
        label = "2. BIOMETRIK & PENGUKURAN",
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
                initialValue = 168,
                fieldName = "Height",
                label = "TINGGI BADAN",
                unit = "cm"
            )
            NumberPickerField(
                modifier = Modifier.weight(1f),
                fieldProperties = FieldProperties(
                    validator = MockNumberValidator(),
                ),
                initialValue = 58,
                fieldName = "Weight",
                label = "BERAT SAAT INI",
                unit = "kg"
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Spacing.s),
            color = colorScheme.surfaceVariant
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.m),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Speed,
                    contentDescription = "accelerate_icon",
                    tint = colorScheme.primary
                )
                Text(
                    stringResource(R.string.body_mass_index),
                    style = typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "21.5",
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                )
                Chip(
                    color = BodyMassIndexStatus.NORMAL.statusColor().first,
                    contentColor = BodyMassIndexStatus.NORMAL.statusColor().second,
                ) {
                    Text(
                        BodyMassIndexStatus.NORMAL.label(),
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
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
    UserProfileContentSection(
        label = "3. AKTIVITAS HARIAN",
        icon = Icons.Default.Info,
        modifier = Modifier.fillMaxWidth()
    ) {
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
        val isSelected = activity == activityState
        Surface(
            shape = RoundedCornerShape(Spacing.s),
            color = if (isSelected) colorScheme.primary else colorScheme.surfaceVariant,
            contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.m),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.m)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) colorScheme.primaryContainer.copy(alpha = 0.25f) else colorScheme.surface,
                    contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
                ) {
                    Icon(
                        modifier = Modifier.padding(Spacing.s),
                        imageVector = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().icon()
                        },
                        contentDescription = "${activity}_icon",
                        tint = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().label().first
                        },
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                        )
                    )
                    Text(
                        text = with(PersonalActivitiesUtility) {
                            activity.toPersonalActivities().label().second
                        },
                        style = typography.bodySmall.copy(
                            color = if (isSelected) colorScheme.onPrimary.copy(alpha = 0.85f) else colorScheme.onSurfaceVariant
                        )
                    )
                }

                if (isSelected) {
                    Surface(
                        shape = CircleShape,
                        color = colorScheme.onPrimary,
                        contentColor = colorScheme.primary
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "selected",
                            modifier = Modifier
                                .padding(Spacing.xxs)
                                .size(Spacing.m),
                            tint = colorScheme.primary
                        )
                    }
                } else {
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(Spacing.xxs / 2, colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Box(modifier = Modifier.size(Spacing.m))
                    }
                }
            }
        }
    }
}


@Composable
private fun DailyEstimationSection() {
    UserProfileContentSection(
        label = "4. TARGET POLA MAKAN & DIET",
        icon = Icons.Default.Info,
    ) {
        DailyEstimationContent()
    }
}

@Composable
private fun DailyEstimationContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        DailyEstimationContentHeader()
        DailyEstimationContentBody()
    }
}

@Composable
private fun DailyEstimationContentHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = colorScheme.secondaryContainer
        ) {
            Icon(
                Icons.Default.LocalFireDepartment,
                modifier = Modifier.padding(Spacing.s),
                contentDescription = "daily_estimation",
                tint = colorScheme.secondary
            )
        }
        Column {
            Text(
                stringResource(R.string.user_profile_daily_estimation_section_title),
                style = typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.primary
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
                Text("TDEE Siap", style = typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            }
            Text(
                "Defisit: -350 kkal",
                style = typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun DailyEstimationContentBody() {
    Button(
        shape = RoundedCornerShape(Spacing.s),
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        )
    ) {
        Icon(
            Icons.Default.Calculate,
            contentDescription = "calculate_nutrients_need",
            modifier = Modifier.padding(end = Spacing.xs)
        )
        Text(
            "Simpan & Hitung Kebutuhan Nutrisi",
            style = typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}


@Preview
@Composable
private fun DailyActivitiesContentPreview() {
    NutritionTrackerTheme {
        DailyEstimationSection()
    }
}
