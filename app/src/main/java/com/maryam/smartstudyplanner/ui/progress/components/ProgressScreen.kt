package com.maryam.smartstudyplanner.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.fillMaxWidth
import com.maryam.smartstudyplanner.ui.progress.components.SubjectProgressRow
import com.maryam.smartstudyplanner.ui.progress.components.TaskStatsRow
import com.maryam.smartstudyplanner.ui.progress.components.WeeklyBarChart
import androidx.compose.material3.ExperimentalMaterial3Api


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(viewModel: ProgressViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Progress") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("This Week", style = MaterialTheme.typography.titleMedium)
            WeeklyBarChart(entries = uiState.weeklyChart)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("This Month", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${uiState.monthlyTotalMinutes} minutes studied",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Text("Task Statistics", style = MaterialTheme.typography.titleMedium)
            TaskStatsRow(
                completed = uiState.completedTasks,
                pending = uiState.pendingTasks,
                overdue = uiState.overdueTasks
            )

            Text(
                "Active Goals: ${uiState.activeGoalsCount}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text("Subject-wise Study Time", style = MaterialTheme.typography.titleMedium)
            if (uiState.subjectProgress.isEmpty()) {
                Text(
                    "No study sessions recorded yet.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                uiState.subjectProgress.forEach { item ->
                    SubjectProgressRow(
                        subjectName = item.subjectName,
                        minutes = item.minutes,
                        fractionOfTotal = item.fraction
                    )
                }
            }
        }
    }
}