package com.maryam.smartstudyplanner.ui.progress.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

data class BarChartEntry(val label: String, val minutes: Long)

@Composable
fun WeeklyBarChart(entries: List<BarChartEntry>) {
    val maxMinutes = (entries.maxOfOrNull { it.minutes } ?: 0L).coerceAtLeast(1L)
    val chartHeight = 140.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(chartHeight + 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        entries.forEach { entry ->
            // Har bar ka fraction (0f..1f) uski height decide karta hai —
            // fillMaxHeight(fraction) ek parent Box ke andar bottom-aligned
            // rakh ke bar jaisa visual effect deta hai, bina Canvas ke.
            val fraction = (entry.minutes.toFloat() / maxMinutes.toFloat()).coerceIn(0.02f, 1f)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.height(chartHeight),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(fraction)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        // bar body — koi content nahi, sirf background color
                    }
                }
                Text(entry.label, style = MaterialTheme.typography.labelSmall)
                Text("${entry.minutes}m", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}