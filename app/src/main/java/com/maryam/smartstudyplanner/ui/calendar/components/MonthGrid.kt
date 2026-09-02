package com.maryam.smartstudyplanner.ui.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.util.daysInMonth
import com.maryam.smartstudyplanner.util.firstDayOffset
import com.maryam.smartstudyplanner.util.isSameDay
import com.maryam.smartstudyplanner.util.startOfDay
import java.util.Calendar

private val weekDayLabels = listOf("S", "M", "T", "W", "T", "F", "S")

@Composable
fun MonthGrid(
    monthStart: Long,
    selectedDate: Long,
    markedDays: Set<Int>,
    onDayClick: (Long) -> Unit
) {
    val totalDays = daysInMonth(monthStart)
    val offset = firstDayOffset(monthStart)
    val today = System.currentTimeMillis()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDayLabels.forEach { label ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        val totalCells = offset + totalDays
        val totalRows = (totalCells + 6) / 7

        for (row in 0 until totalRows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val dayNumber = cellIndex - offset + 1

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayNumber in 1..totalDays) {
                            val cellDate = dateForDay(monthStart, dayNumber)
                            val isSelected = isSameDay(cellDate, selectedDate)
                            val isToday = isSameDay(cellDate, today)
                            val isMarked = markedDays.contains(dayNumber)

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else if (isToday) MaterialTheme.colorScheme.secondaryContainer
                                        else androidx.compose.ui.graphics.Color.Transparent
                                    )
                                    .clickable { onDayClick(cellDate) }
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (isMarked) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                else MaterialTheme.colorScheme.primary
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun dateForDay(monthStart: Long, day: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = monthStart
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return startOfDay(calendar.timeInMillis)
}