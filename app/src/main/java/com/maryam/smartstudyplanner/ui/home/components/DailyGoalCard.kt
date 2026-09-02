package com.maryam.smartstudyplanner.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity

@Composable
fun DailyGoalCard(
    studyMinutes: Long,
    activeGoal: StudyGoalEntity?,
    goalProgressMinutes: Long
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Today's Study Time", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$studyMinutes min studied today",
                style = MaterialTheme.typography.bodyMedium
            )

            if (activeGoal != null) {
                Spacer(modifier = Modifier.height(8.dp))
                val progress = if (activeGoal.targetMinutes == 0L) {
                    0f
                } else {
                    (goalProgressMinutes.toFloat() / activeGoal.targetMinutes.toFloat()).coerceIn(0f, 1f)
                }
                Text(
                    text = "Goal: ${activeGoal.title} — $goalProgressMinutes/${activeGoal.targetMinutes} min",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "No active goal set yet.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}