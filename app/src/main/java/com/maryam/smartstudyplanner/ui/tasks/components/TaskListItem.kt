package com.maryam.smartstudyplanner.ui.tasks.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskPriority
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Edit


private fun priorityColor(priority: TaskPriority): Color = when (priority) {
    TaskPriority.LOW -> Color(0xFF4CAF50)
    TaskPriority.MEDIUM -> Color(0xFFFF9800)
    TaskPriority.HIGH -> Color(0xFFE53935)
}

@Composable
fun TaskListItem(
    task: TaskEntity,
    subjectName: String,
    onToggleComplete: () -> Unit,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status == TaskStatus.COMPLETED,
                onCheckedChange = { onToggleComplete() }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.status == TaskStatus.COMPLETED) TextDecoration.LineThrough else null
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(subjectName, style = MaterialTheme.typography.bodyMedium)
                    task.dueDate?.let {
                        Text(
                            text = "  •  Due ${dateFormat.format(it)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Text(
                    text = task.priority.name,
                    color = priorityColor(task.priority),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit task")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete task")
            }
        }
    }
}