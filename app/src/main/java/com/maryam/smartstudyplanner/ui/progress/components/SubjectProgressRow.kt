package com.maryam.smartstudyplanner.ui.progress.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SubjectProgressRow(
    subjectName: String,
    minutes: Long,
    fractionOfTotal: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(subjectName, style = MaterialTheme.typography.bodyLarge)
        }
        Text("$minutes min", style = MaterialTheme.typography.bodyMedium)
        LinearProgressIndicator(
            progress = { fractionOfTotal.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}