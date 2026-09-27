package com.example.nutritiontracker.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritiontracker.ui.theme.colorScheme
import com.example.nutritiontracker.ui.theme.typography
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


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

@Composable
private fun rememberDateBuild(
    year: Int = 2026,
    formatter: String = currentFormatter,
    month: Int = 1,
): List<DatePickerHolder?>? {

    val dates = remember(year, month) {
        LocalDate.of(year, month, 1)
            .datesUntil(LocalDate.of(year + 1, month, 1))
            .map {
                val currentDate = it.format(formatDate(formatter)).split("-")

                val year = currentDate[0]
                val month = currentDate[1]
                val day = currentDate[2]
                val dayName = currentDate[3]
                DatePickerHolder(year = year, month = month, day = day, dayName = dayName)
            }
            .toList()
    }
    return dates
}

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
    var trackedMonth by remember { mutableStateOf("") }

    var selectedDate by remember {
        mutableStateOf(
            Instant.now().atZone(ZoneId.systemDefault()).format(formatDate()).toString()
        )
    }

    LaunchedEffect(Unit) {
        val firstDateIndex = dates?.indexOfFirst { date ->
            date?.let {
                val (year, month, day, dayName) = date
                "$year-$month-$day-$dayName" == selectedDate
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
                            trackedMonth = currentDate.month
                            Log.d("[DATE_PICKER]", "NOT SCROLLING")
                        }
                    }
                }
        }

    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        //TODO add dropdown button for selected month
        Text(trackedMonth)
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
            flingBehavior = snapFlingBehavior,
        ) {
            dates?.let {
                items(dates) { date ->
                    date?.let {
                        val (year, month, day, dayName) = date
                        DateTimePickerCard(dayName = dayName, day = day, onTap = {
                            selectedDate = "$year-$month-$day-$dayName"
                        }, isActive = selectedDate == "$year-$month-$day-$dayName")
                    }
                }
            }
        }
    }
}

@Preview(name = "DatePickerCardNonActive", showBackground = true)
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
    DateTimePickerCard(isActive = true)
}