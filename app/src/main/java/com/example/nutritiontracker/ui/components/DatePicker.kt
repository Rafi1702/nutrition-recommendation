package com.example.nutritiontracker.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.nutritiontracker.ui.theme.NutritionTrackerTheme
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
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

private data class DatePickerHolder(
    val year: String,
    val month: String,
    val day: String,
    val dayName: String
)


const val currentFormatter = "yyyy-MMMM-dd-EEEE"


private fun formatDate(
    formatter: String = currentFormatter,
    locale: Locale = Locale.forLanguageTag("id-ID")
): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(formatter, locale)
}

private fun LocalDate.createDatePicker(): DatePickerHolder {
    val currentDate = this.format(formatDate(currentFormatter)).split("-")
    val year = currentDate[0]
    val month = currentDate[1]
    val day = currentDate[2]
    val dayName = currentDate[3]
    return DatePickerHolder(year = year, month = month, day = day, dayName = dayName)
}

@Composable
private fun rememberDateBuild(
    year: Int = 2026,

    month: Int = 1,
): List<DatePickerHolder?>? {
    val dates = remember(year, month) {
        LocalDate.of(year, month, 1)
            .datesUntil(LocalDate.of(year + 1, month, 1))
            .map {
                it.createDatePicker()
            }
            .toList()
    }
    return dates
}


private val DatePickerHolderSaver = listSaver<DatePickerHolder, Any>(

    save = { date ->
        listOf(date.year, date.month, date.day, date.dayName)
    },

    restore = { restoredList ->
        DatePickerHolder(
            year = restoredList[0] as String,
            month = restoredList[1] as String,
            day = restoredList[2] as String,
            dayName = restoredList[3] as String
        )
    }
)

@Preview(showBackground = true)
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    year: Int = 2026,
    month: Int = 1,
) {
    val dates = rememberDateBuild(year = year, month = month)
    val listState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    var selectedDate by rememberSaveable(stateSaver = DatePickerHolderSaver) {
        mutableStateOf(
            Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().createDatePicker()
        )
    }
    val trackedMonth = rememberSaveable(selectedDate, stateSaver = DatePickerHolderSaver) {
        mutableStateOf(selectedDate)
    }

    LaunchedEffect(Unit) {
        val firstDateIndex = dates?.indexOfFirst { date ->
            date?.let {
                selectedDate == date
            } == true
        }

        firstDateIndex?.let { value ->
            if (value != -1) {
                listState.scrollToItem(value)
            }
        }

        dates?.let {

            snapshotFlow { listState.isScrollInProgress }
                .collect { isScrolling ->
                    if (!isScrolling) {
                        val currentDate = dates[listState.firstVisibleItemIndex]

                        currentDate?.let {
                            trackedMonth.value = currentDate
                            Log.d("[DATE_PICKER]", "NOT SCROLLING")
                        }
                    }
                }
        }

    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        //TODO add dropdown button for selected month
        Text(trackedMonth.value.month)
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
            flingBehavior = snapFlingBehavior,
        ) {
            dates?.let {
                items(dates) { date ->
                    date?.let {
                        val (_, _, day, dayName) = date
                        DateTimePickerCard(dayName = dayName, day = day, onTap = {
                            selectedDate = date
                        }, isActive = selectedDate == date)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DatePickerField(){
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selectedDate,
            onValueChange = { },
            label = "Date",
            isSecure =  false,
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

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(colorScheme.surface)
                        .padding(16.dp)
                ) {
                    M3DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    return ""
}

@Composable
private fun DateTimePickerCard(
    dayName: String = "Thu",
    day: String = "01",
    onTap: () -> Unit = {},
    isActive: Boolean = false
) {

    Card(
        modifier = Modifier
            .clickable(onClick = onTap)
            .size(72.dp),
        shape = RoundedCornerShape(8.dp),
        colors = DatePickerDefaults.getDatePickerColor(isActive)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(dayName, style = typography.labelLarge)
            Text(day, style = typography.labelMedium)
        }
    }
}

@Preview(name = "DateTimePickerActive", showBackground = true)
@Composable
private fun DateTimePickerActive() {
    NutritionTrackerTheme { DateTimePickerCard(isActive = true) }
}