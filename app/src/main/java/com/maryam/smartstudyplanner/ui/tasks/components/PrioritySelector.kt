package com.maryam.smartstudyplanner.ui.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.maryam.smartstudyplanner.data.local.entity.TaskPriority
import androidx.compose.ui.unit.dp

@Composable
fun PrioritySelector(
    selected: TaskPriority,
    onSelect: (TaskPriority) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskPriority.entries.forEach { priority ->
            FilterChip(
                selected = selected == priority,
                onClick = { onSelect(priority) },
                label = { Text(priority.name) }
            )
        }
    }
}