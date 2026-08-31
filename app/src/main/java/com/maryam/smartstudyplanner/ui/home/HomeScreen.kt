package com.maryam.smartstudyplanner.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.ui.home.components.DailyGoalCard
import com.maryam.smartstudyplanner.ui.home.components.HomeTaskRow
import com.maryam.smartstudyplanner.ui.home.components.QuickActionsRow
import com.maryam.smartstudyplanner.ui.home.components.TodayProgressCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("${uiState.greeting}!") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                onStartSessionClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Study sessions are coming in the next phase")
                    }
                }
            )

            TodayProgressCard(
                completed = uiState.completedTodayCount,
                total = uiState.totalTodayCount
            )

            DailyGoalCard(
                studyMinutes = uiState.todayStudyMinutes,
                activeGoal = uiState.activeGoal
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