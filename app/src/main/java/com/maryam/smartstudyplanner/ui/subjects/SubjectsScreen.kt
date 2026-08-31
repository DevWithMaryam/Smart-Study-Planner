package com.maryam.smartstudyplanner.ui.subjects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.ui.components.ConfirmDeleteDialog
import com.maryam.smartstudyplanner.ui.subjects.components.AddEditSubjectDialog
import com.maryam.smartstudyplanner.ui.subjects.components.SubjectListItem
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    onSubjectClick: (Long) -> Unit,
    viewModel: SubjectsViewModel = hiltViewModel()
) {
    val subjects by viewModel.subjects.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingSubject by remember { mutableStateOf<SubjectEntity?>(null) }
    var subjectToDelete by remember { mutableStateOf<SubjectEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Subjects") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingSubject = null
                showAddEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add subject")
            }
        }
    ) { innerPadding ->
        if (subjects.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No subjects yet. Tap + to add your first subject.",
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
                items(subjects, key = { it.subjectId }) { subject ->
                    SubjectListItem(
                        subject = subject,
                        onClick = { onSubjectClick(subject.subjectId) },
                        onEditClick = {
                            editingSubject = subject
                            showAddEditDialog = true
                        },
                        onDeleteClick = { subjectToDelete = subject }
                    )
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditSubjectDialog(
            subject = editingSubject,
            onDismiss = { showAddEditDialog = false },
            onConfirm = { name, description ->
                val current = editingSubject
                if (current == null) {
                    viewModel.addSubject(name, description)
                } else {
                    viewModel.updateSubject(current.copy(name = name, description = description))
                }
                showAddEditDialog = false
            }
        )
    }

    subjectToDelete?.let { subject ->
        ConfirmDeleteDialog(
            title = "Delete subject?",
            message = "This will also delete all topics and tasks under \"${subject.name}\".",
            onConfirm = {
                viewModel.deleteSubject(subject)
                subjectToDelete = null
            },
            onDismiss = { subjectToDelete = null }
        )
    }
}