package com.example.nutritiontracker.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.components.textfield.TextFormField
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.Spacing
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import com.example.nutritiontracker.ui.utils.DatePickerHolder
import com.example.nutritiontracker.ui.utils.createDatePicker
import com.example.nutritiontracker.ui.utils.currentLocalDate
import com.example.nutritiontracker.ui.utils.formatDate
import com.example.nutritiontracker.ui.utils.formatWith
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KProperty1
import kotlin.time.ExperimentalTime
import androidx.compose.material3.DatePicker as M3DatePicker

object DatePickerDefaults {
    @Composable
    fun getDatePickerColor(isActive: Boolean = false): CardColors {
        return if (isActive) {
            CardDefaults.cardColors(
                contentColor = colorScheme.onPrimary,
                containerColor = colorScheme.primary
            )
        } else {
            CardDefaults.cardColors()
        }
    }
}




@OptIn(kotlin.time.ExperimentalTime::class)
private fun String.toMillisEpoch(): Long? {
    return try {
        LocalDate.parse(this)
            .atStartOfDayIn(TimeZone.UTC)
            .toEpochMilliseconds()
    } catch (_: Exception) {
        null
    }
}


@Composable
private fun rememberDateBuild(
    year: Int = 2026,
    month: Int = 1,
): List<DatePickerHolder> {
    return remember(year, month) {
        val start = LocalDate(year, month, 1)
        val end = LocalDate(year + 1, month, 1)
        val datesList = mutableListOf<DatePickerHolder>()
        var curr = start
        while (curr < end) {
            datesList.add(curr.createDatePicker())
            curr = curr.plus(DatePeriod(days = 1))
        }
        datesList
    }
}


@Composable
private fun rememberDateBuild(
    /**
     * @param initialDate the String used ISO(yyyy-MM-dd)
     */
    initialDate: String,
): List<DatePickerHolder> {
    if (initialDate.isEmpty()) return emptyList()

    val date = initialDate.split("-")
    val year = date.getOrNull(0)?.toIntOrNull() ?: 2026
    val month = date.getOrNull(1)?.toIntOrNull() ?: 1
    return rememberDateBuild(
        year = year,
        month = month,
    )
}

private val DatePickerHolderSaver = listSaver<DatePickerHolder, Any>(
    save = { date ->
        listOf(date.year, date.month, date.day, date.dayName, date.yyMMDD)
    },
    restore = { restoredList ->
        DatePickerHolder(
            year = restoredList[0] as String,
            month = restoredList[1] as String,
            day = restoredList[2] as String,
            dayName = restoredList[3] as String,
            yyMMDD = restoredList[4] as String,
        )
    }
)

@OptIn(ExperimentalTime::class)
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    onDateChanged: (String) -> Unit = {},
    initialValue: String = currentLocalDate().formatWith(),
) {
    val dates = rememberDateBuild(initialValue)
    val listState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    var selectedDate by rememberSaveable(stateSaver = DatePickerHolderSaver) {
        mutableStateOf(
            initialValue.createDatePicker()
        )
    }
    val trackedMonth = rememberSaveable(stateSaver = DatePickerHolderSaver) {
        mutableStateOf(selectedDate)
    }

    LaunchedEffect(initialValue) {
        if (initialValue.isNotEmpty()) {
            val dateHolder = initialValue.createDatePicker()
            selectedDate = dateHolder
            trackedMonth.value = dateHolder
            val index = dates.indexOfFirst { it.yyMMDD == dateHolder.yyMMDD }
            if (index != -1) {
                listState.scrollToItem(index)
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                dates.getOrNull(index)?.let { currentDate ->
                    trackedMonth.value = currentDate
                }
            }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.xxs),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colorScheme.primaryContainer.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = trackedMonth.value.month,
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    )
                    Text(
                        text = trackedMonth.value.year,
                        style = typography.labelSmall.copy(
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.secondaryContainer.copy(alpha = 0.5f)
            ) {
                Text(
                    text = "${selectedDate.day} ${selectedDate.month.take(3)}",
                    modifier = Modifier.padding(
                        horizontal = Spacing.s,
                        vertical = Spacing.xxs + 2.dp
                    ),
                    style = typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSecondaryContainer
                    )
                )
            }
        }
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s),
            contentPadding = contentPadding,
            flingBehavior = snapFlingBehavior,
        ) {
            items(
                items = dates,
                key = { date -> date.yyMMDD }
            ) { date ->
                DateTimePickerCard(
                    dayName = date.dayName,
                    day = date.day,
                    monthName = date.month.take(3),
                    onTap = {
                        selectedDate = date
                        trackedMonth.value = date
                        onDateChanged(date.yyMMDD)
                    },
                    isActive = selectedDate.yyMMDD == date.yyMMDD
                )
            }
        }
    }
}


@OptIn(kotlin.time.ExperimentalTime::class)
private fun Long.toLocalDateString(): String {
    return kotlin.time.Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .date
        .toString()
}

@OptIn(ExperimentalMaterial3Api::class, kotlin.time.ExperimentalTime::class)
@Composable
fun <T : Any> DatePickerField(
    fieldName: KProperty1<T, String>,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val formBuilder = currentForm<T>()
    val datePickerState = rememberDatePickerState()

    val selectedDateString = remember(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.toLocalDateString()
    }

    val existingField = remember {
        formBuilder.getField(fieldName)?.copy(
            initialValue = selectedDateString ?: currentLocalDate().formatWith()
        ) ?: FieldProperties(validator = listOf())
    }

    val isDirty = remember {
        derivedStateOf {
            (existingField.state.value != selectedDateString) && selectedDateString != null
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        FieldRegister(
            name = fieldName,
            fieldProperties = existingField,
        ) {
            TextFormField(
                value = existingField.state.value ?: "",
                readOnly = true,
                label = label,
                enabled = false,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis = existingField.state.value?.toMillisEpoch()
                },
                confirmButton = {
                    Button(
                        enabled = isDirty.value,
                        onClick = {
                            selectedDateString?.let { selectedDateState ->
                                existingField.onValueStateChange(selectedDateState)
                                showDatePicker = false
                            }
                        }) { Text("Submit") }
                },
                dismissButton = {
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = colorScheme.errorContainer
                        ),
                        onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis =
                                existingField.state.value?.toMillisEpoch()
                        }) {
                        Text("Dismiss")
                    }
                }
            ) {
                M3DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DatePickerPreview() {
    NutritionTrackerTheme {
        DatePicker()
    }
}

@Composable
private fun DateTimePickerCard(
    dayName: String = "Thu",
    day: String = "01",
    monthName: String? = null,
    onTap: () -> Unit = {},
    isActive: Boolean = false
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onTap)
            .size(80.dp),
        shape = RoundedCornerShape(Spacing.s),
        colors = DatePickerDefaults.getDatePickerColor(isActive)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            monthName?.let {
                Text(monthName, style = typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(Spacing.xxs))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(dayName, style = typography.labelLarge)
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text(day, style = typography.labelMedium)
        }
    }
}

@Preview(name = "DateTimePickerActive", showBackground = true)
@Composable
private fun DateTimePickerActive() {
    NutritionTrackerTheme { DateTimePickerCard(isActive = true, monthName = "Jan") }
}