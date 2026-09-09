package com.maryam.smartstudyplanner.ui.progress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

@Composable
fun TaskStatsRow(completed: Int, pending: Int, overdue: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard("Completed", completed)
        StatCard("Pending", pending)
        StatCard("Overdue", overdue)
    }
}

@Composable
private fun StatCard(label: String, value: Int) {
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(value.toString(), style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}