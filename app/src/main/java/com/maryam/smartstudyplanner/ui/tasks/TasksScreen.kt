package com.maryam.smartstudyplanner.ui.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.ui.components.ConfirmDeleteDialog
import com.maryam.smartstudyplanner.ui.components.EmptyState
import com.maryam.smartstudyplanner.ui.tasks.components.TaskListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onAddTaskClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val subjectNames by viewModel.subjectNames.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tasks") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search tasks") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilter.entries.forEach { f ->
                    FilterChip(
                        selected = filter == f,
                        onClick = { viewModel.setFilter(f) },
                        label = { Text(f.name) }
                    )
                }
            }

            if (tasks.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.CheckCircle,
                    title = "No tasks found",
                    subtitle = if (searchQuery.isNotBlank()) {
                        "Try a different search or filter."
                    } else {
                        "Tap + to add your first task."
                    }
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(tasks, key = { it.taskId }) { task ->
                        TaskListItem(
                            task = task,
                            subjectName = subjectNames[task.subjectId] ?: "",
                            onToggleComplete = { viewModel.toggleCompletion(task) },
                            onClick = { onTaskClick(task.taskId) },
                            onDeleteClick = { taskToDelete = task },
                           // modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    taskToDelete?.let { task ->
        ConfirmDeleteDialog(
            title = "Delete task?",
            message = "\"${task.title}\" will be permanently deleted.",
            onConfirm = {
                viewModel.deleteTask(task)
                taskToDelete = null
            },
            onDismiss = { taskToDelete = null }
        )
    }
}