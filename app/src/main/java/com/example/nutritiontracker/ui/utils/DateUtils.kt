package com.example.nutritiontracker.ui.utils

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

const val displayFormatter = "yyyy-MMMM-dd-EEEE"
const val serviceFormatter = "yyyy-MM-dd"

@OptIn(ExperimentalTime::class)
val instant: LocalDate = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date

@OptIn(FormatStringsInDatetimeFormats::class)
fun     formatDate(
    pattern: String = displayFormatter,
): DateTimeFormat<LocalDate> = LocalDate.Format {
    byUnicodePattern(pattern)
}

fun nowYyyyMMDD(formatter: String = serviceFormatter): String {
    return instant
        .format(formatDate(formatter))
}

fun LocalDate.minusDays(other: LocalDate): LocalDate {
    val diffDays = other.daysUntil(this)
    return this.minus(DatePeriod(days = diffDays))
}

