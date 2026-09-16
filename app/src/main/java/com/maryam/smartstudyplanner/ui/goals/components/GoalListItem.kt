package com.maryam.smartstudyplanner.ui.goals.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity

@Composable
fun GoalListItem(
    goal: StudyGoalEntity,
    subjectName: String?,
    progressMinutes: Long,
    isExpired: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (goal.targetMinutes == 0L) {
        0f
    } else {
        (progressMinutes.toFloat() / goal.targetMinutes.toFloat()).coerceIn(0f, 1f)
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(goal.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = subjectName ?: "All subjects",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit goal")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete goal")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$progressMinutes / ${goal.targetMinutes} min" +
                        if (isExpired) " • Expired" else "",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}