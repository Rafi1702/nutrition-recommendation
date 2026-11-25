package com.example.nutritiontracker.ui.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

enum class DateFormatter(
    val stringFormat: String,
) {
    SERVICE_FORMATTER("yyyy-MM-dd"),
//    SERVICE_FORMATTER("yyyy-MM-dd")
}


data class DateRange(
    val startDate: String,
    val endDate: String
)

@OptIn(ExperimentalTime::class)
val instant: LocalDate = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

@OptIn(FormatStringsInDatetimeFormats::class)
fun formatDate(
    pattern: String = DateFormatter.SERVICE_FORMATTER.stringFormat,
): DateTimeFormat<LocalDate> = LocalDate.Format {
    byUnicodePattern(pattern)
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun dateTimeFormat(
    pattern: DateFormatter = DateFormatter.SERVICE_FORMATTER,
): DateTimeFormat<LocalDate> = formatDate(pattern.stringFormat)


@OptIn(ExperimentalTime::class)
fun currentLocalDate(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    clock: Clock = Clock.System,
): LocalDate = clock.now().toLocalDateTime(timeZone).date

fun LocalDate.formatWith(
    formatter: DateFormatter = DateFormatter.SERVICE_FORMATTER,
): String = format(dateTimeFormat(formatter))

fun LocalDate.rangeToFormattedRange(
    other: LocalDate,
    formatter: DateFormatter = DateFormatter.SERVICE_FORMATTER,
): DateRange {
    val start = minOf(this, other)
    val end = maxOf(this, other)
    return DateRange(
        startDate = start.formatWith(formatter),
        endDate = end.formatWith(formatter),
    )
}

fun LocalDate.minusDays(days: Int): LocalDate =
    this.minus(DatePeriod(days = days))

fun LocalDate.plusDays(days: Int): LocalDate =
    this.plus(DatePeriod(days = days))

@OptIn(ExperimentalTime::class)
fun daysAgo(
    days: Int,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    clock: Clock = Clock.System,
): LocalDate = clock.now()
    .toLocalDateTime(timeZone)
    .date
    .minusDays(days)

@OptIn(ExperimentalTime::class)
fun daysAfter(
    days: Int,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    clock: Clock = Clock.System,
): LocalDate = clock.now()
    .toLocalDateTime(timeZone)
    .date
    .plusDays(days)

fun String.rangeToFormattedRange(
    other: String,
    inputFormat: DateTimeFormat<LocalDate> = dateTimeFormat(DateFormatter.SERVICE_FORMATTER),
    outputFormat: DateFormatter = DateFormatter.SERVICE_FORMATTER,
): DateRange {
    require(this.isNotBlank() && other.isNotBlank()) {
        "Start and end date strings must not be blank"
    }

    val start = LocalDate.parse(this, inputFormat)
    val end = LocalDate.parse(other, inputFormat)

    require(end >= start) {
        "End date ($end) cannot be before start date ($start)"
    }

    return DateRange(
        startDate = start.formatWith(outputFormat),
        endDate = end.formatWith(outputFormat),
    )
}


