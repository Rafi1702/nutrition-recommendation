package com.example.nutritiontracker.ui.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

const val displayFormatter = "yyyy-MMMM-dd-EEEE"
const val serviceFormatter = "yyyy-MM-dd"

val instant: LocalDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

fun formatDate(
    formatter: String = displayFormatter,
    locale: Locale = Locale.forLanguageTag("id-ID")
): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(formatter, locale)
}

fun nowYyyyMMDD(formatter: String = serviceFormatter): String {
    return instant
        .format(formatDate(formatter))
}

