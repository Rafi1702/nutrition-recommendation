package com.example.nutritiontracker.ui.profile.page

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import com.example.nutritiontracker.R
import com.example.nutritiontracker.domain.model.BodyMassIndexStatus
import com.example.nutritiontracker.domain.model.Gender
import com.example.nutritiontracker.domain.model.PersonalActivities
import com.example.nutritiontracker.ui.theme.colorScheme
import kotlin.collections.indexOf

@Composable
fun String.toGender(): Gender {
    val gendersResource = stringArrayResource(R.array.personal_gender)

    val indexOfResource = gendersResource.indexOf(this)
    return when (indexOfResource) {
        0 -> Gender.MALE
        1 -> Gender.FEMALE
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