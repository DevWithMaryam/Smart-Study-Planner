package com.maryam.smartstudyplanner.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun startOfToday(): Long = startOfDay(System.currentTimeMillis())

fun endOfToday(): Long = endOfDay(System.currentTimeMillis())

fun formatElapsedTime(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun startOfDay(millis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun endOfDay(millis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}

fun startOfMonth(millis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return startOfDay(calendar.timeInMillis)
}

fun endOfMonth(millis: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    return endOfDay(calendar.timeInMillis)
}

fun addMonths(millis: Long, amount: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.add(Calendar.MONTH, amount)
    return startOfMonth(calendar.timeInMillis)
}

fun daysInMonth(millis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun firstDayOffset(millis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = startOfMonth(millis)
    return calendar.get(Calendar.DAY_OF_WEEK) - 1
}

fun dayOfMonth(millis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun isSameDay(a: Long, b: Long): Boolean = startOfDay(a) == startOfDay(b)

fun monthYearLabel(millis: Long): String =
    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(millis)

fun dayHeaderLabel(millis: Long): String =
    SimpleDateFormat("EEEE, dd MMM", Locale.getDefault()).format(millis)

// Aaj se "daysAgo" din pehle ka start-of-day timestamp deta hai.
// Weekly chart ke liye "pichle 7 din" ki range banane mein use hota hai.
fun startOfDaysAgo(daysAgo: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    return startOfDay(calendar.timeInMillis)
}

fun shortDayLabel(millis: Long): String =
    SimpleDateFormat("EEE", Locale.getDefault()).format(millis)