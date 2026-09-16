package com.maryam.smartstudyplanner.ui.study.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SessionListItem(
    session: StudySessionEntity,
    subjectName: String,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    Card(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(subjectName, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${session.durationMinutes} min • ${dateFormat.format(session.date)}",
                style = MaterialTheme.typography.bodyMedium
            )
            session.notes?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}