package com.maryam.smartstudyplanner.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
//-import androidx.compose.foundation.layout.weight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuickActionsRow(
    onAddTaskClick: () -> Unit,
    onStartSessionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = onAddTaskClick, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text("Add Task")
        }
        Button(onClick = onStartSessionClick, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Text("Study Session")
        }
    }
}