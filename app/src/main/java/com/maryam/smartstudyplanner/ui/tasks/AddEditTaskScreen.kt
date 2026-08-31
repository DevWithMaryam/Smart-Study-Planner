package com.maryam.smartstudyplanner.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.maryam.smartstudyplanner.ui.tasks.components.DropdownField
import com.maryam.smartstudyplanner.ui.tasks.components.DueDatePickerField
import com.maryam.smartstudyplanner.ui.tasks.components.PrioritySelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val selectedSubjectId by viewModel.selectedSubjectId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.isEditMode) "Edit Task" else "Add Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.save(onNavigateBack) }) {
                        Text("Save")
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
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title") },
                isError = viewModel.titleError,
                supportingText = { if (viewModel.titleError) Text("Title cannot be empty") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Description (optional)") },
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            DropdownField(
                label = "Subject",
                items = subjects,
                selectedItem = subjects.find { it.subjectId == selectedSubjectId },
                itemLabel = { it.name },
                isError = viewModel.subjectError,
                onItemSelected = { viewModel.onSubjectSelected(it.subjectId) }
            )

            if (topics.isNotEmpty()) {
                DropdownField(
                    label = "Topic (optional)",
                    items = topics,
                    selectedItem = topics.find { it.topicId == viewModel.selectedTopicId },
                    itemLabel = { it.name },
                    onItemSelected = { viewModel.onTopicSelected(it.topicId) }
                )
            }

            Text("Priority")
            PrioritySelector(
                selected = viewModel.priority,
                onSelect = viewModel::onPriorityChange
            )

            DueDatePickerField(
                selectedDate = viewModel.dueDate,
                onDateSelected = viewModel::onDueDateChange
            )
        }
    }
}