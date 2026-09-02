package com.maryam.smartstudyplanner.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import com.maryam.smartstudyplanner.ui.components.ConfirmDeleteDialog
import com.maryam.smartstudyplanner.ui.goals.components.AddEditGoalDialog
import com.maryam.smartstudyplanner.ui.goals.components.GoalListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onNavigateBack: () -> Unit,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val goalsWithProgress by viewModel.goalsWithProgress.collectAsState()
    val subjects by viewModel.subjects.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<StudyGoalEntity?>(null) }
    var goalToDelete by remember { mutableStateOf<StudyGoalEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Goals") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingGoal = null
                showAddEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add goal")
            }
        }
    ) { innerPadding ->
        if (goalsWithProgress.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No goals yet. Tap + to set your first study goal.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(goalsWithProgress, key = { it.goal.goalId }) { item ->
                    GoalListItem(
                        goal = item.goal,
                        subjectName = subjects.find { it.subjectId == item.goal.subjectId }?.name,
                        progressMinutes = item.progressMinutes,
                        isExpired = item.isExpired,
                        onEditClick = {
                            editingGoal = item.goal
                            showAddEditDialog = true
                        },
                        onDeleteClick = { goalToDelete = item.goal }
                    )
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditGoalDialog(
            goal = editingGoal,
            subjects = subjects,
            onDismiss = { showAddEditDialog = false },
            onConfirm = { title, targetMinutes, subjectId, startDate, endDate ->
                val current = editingGoal
                if (current == null) {
                    viewModel.addGoal(title, targetMinutes, subjectId, startDate, endDate)
                } else {
                    viewModel.updateGoal(
                        current.copy(
                            title = title,
                            targetMinutes = targetMinutes,
                            subjectId = subjectId,
                            startDate = startDate,
                            endDate = endDate
                        )
                    )
                }
                showAddEditDialog = false
            }
        )
    }

    goalToDelete?.let { goal ->
        ConfirmDeleteDialog(
            title = "Delete goal?",
            message = "\"${goal.title}\" will be permanently deleted.",
            onConfirm = {
                viewModel.deleteGoal(goal)
                goalToDelete = null
            },
            onDismiss = { goalToDelete = null }
        )
    }
}