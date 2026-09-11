package com.maryam.smartstudyplanner.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.maryam.smartstudyplanner.ui.home.components.DailyGoalCard
import com.maryam.smartstudyplanner.ui.home.components.HomeTaskRow
import com.maryam.smartstudyplanner.ui.home.components.QuickActionsRow
import com.maryam.smartstudyplanner.ui.home.components.TodayProgressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onStartSessionClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${uiState.greeting}!") },
                actions = {
                    IconButton(onClick = onGoalsClick) {
                        Icon(Icons.Default.Star, contentDescription = "Study goals")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionsRow(
                onAddTaskClick = onAddTaskClick,
                onStartSessionClick = onStartSessionClick
            )

            TodayProgressCard(
                completed = uiState.completedTodayCount,
                total = uiState.totalTodayCount
            )

            DailyGoalCard(
                studyMinutes = uiState.todayStudyMinutes,
                activeGoal = uiState.activeGoal,
                goalProgressMinutes = uiState.activeGoalProgressMinutes
            )

            Text("Today's Tasks", style = MaterialTheme.typography.titleMedium)
            if (uiState.todayTasks.isEmpty()) {
                Text("No tasks due today.", style = MaterialTheme.typography.bodyMedium)
            } else {
                uiState.todayTasks.forEach { task ->
                    HomeTaskRow(
                        task = task,
                        subjectName = uiState.subjectNames[task.subjectId] ?: "",
                        onClick = { onTaskClick(task.taskId) }
                    )
                }
            }

            Text("Upcoming Tasks", style = MaterialTheme.typography.titleMedium)
            if (uiState.upcomingTasks.isEmpty()) {
                Text("Nothing upcoming.", style = MaterialTheme.typography.bodyMedium)
            } else {
                uiState.upcomingTasks.forEach { task ->
                    HomeTaskRow(
                        task = task,
                        subjectName = uiState.subjectNames[task.subjectId] ?: "",
                        onClick = { onTaskClick(task.taskId) }
                    )
                }
            }
        }
    }
}