package com.maryam.smartstudyplanner.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.ui.calendar.components.MonthGrid
import com.maryam.smartstudyplanner.ui.home.components.HomeTaskRow
import com.maryam.smartstudyplanner.ui.study.components.SessionListItem
import com.maryam.smartstudyplanner.util.dayHeaderLabel
import com.maryam.smartstudyplanner.util.monthYearLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onTaskClick: (Long) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val monthStart by viewModel.displayedMonthStart.collectAsState()
    val markedDays by viewModel.markedDays.collectAsState()
    val tasks by viewModel.selectedDayTasks.collectAsState()
    val sessions by viewModel.selectedDaySessions.collectAsState()
    val subjectNames by viewModel.subjectNames.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Calendar") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { viewModel.goToPreviousMonth() }) { Text("<") }
                Text(monthYearLabel(monthStart), style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { viewModel.goToNextMonth() }) { Text(">") }
            }

            MonthGrid(
                monthStart = monthStart,
                selectedDate = selectedDate,
                markedDays = markedDays,
                onDayClick = { viewModel.selectDate(it) }
            )

            Text(dayHeaderLabel(selectedDate), style = MaterialTheme.typography.titleMedium)

            Text("Tasks", style = MaterialTheme.typography.titleMedium)
            if (tasks.isEmpty()) {
                Text("No tasks due on this day.", style = MaterialTheme.typography.bodyMedium)
            } else {
                tasks.forEach { task ->
                    HomeTaskRow(
                        task = task,
                        subjectName = subjectNames[task.subjectId] ?: "",
                        onClick = { onTaskClick(task.taskId) }
                    )
                }
            }

            Text("Study Sessions", style = MaterialTheme.typography.titleMedium)
            if (sessions.isEmpty()) {
                Text("No sessions recorded on this day.", style = MaterialTheme.typography.bodyMedium)
            } else {
                sessions.forEach { session ->
                    SessionListItem(
                        session = session,
                        subjectName = subjectNames[session.subjectId] ?: ""
                    )
                }
            }
        }
    }
}