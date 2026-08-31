package com.maryam.smartstudyplanner.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeTaskRow(task: TaskEntity, subjectName: String, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            Text(subjectName, style = MaterialTheme.typography.bodyMedium)
            task.dueDate?.let {
                Text("Due: ${dateFormat.format(it)}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}