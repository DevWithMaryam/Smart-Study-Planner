package com.maryam.smartstudyplanner.ui.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.ui.tasks.components.DropdownField
import com.maryam.smartstudyplanner.util.formatElapsedTime
import androidx.compose.material.icons.filled.List


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    onNavigateBack: () -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: StudySessionViewModel = hiltViewModel()
) {
    val subjects by viewModel.subjects.collectAsState()
    val topics by viewModel.topics.collectAsState()
    val selectedSubjectId by viewModel.selectedSubjectId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Session") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onHistoryClick) {
                        Icon(Icons.Default.List, contentDescription = "Session history")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (viewModel.phase) {
                SessionPhase.SETUP -> SetupContent(
                    subjects = subjects,
                    topics = topics,
                    selectedSubjectId = selectedSubjectId,
                    selectedTopicId = viewModel.selectedTopicId,
                    subjectError = viewModel.subjectError,
                    onSubjectSelected = viewModel::onSubjectSelected,
                    onTopicSelected = viewModel::onTopicSelected,
                    onStart = viewModel::startSession
                )

                SessionPhase.RUNNING -> RunningContent(
                    elapsedSeconds = viewModel.elapsedSeconds,
                    onFinish = viewModel::finishSession
                )

                SessionPhase.FINISHED -> SummaryContent(
                    elapsedSeconds = viewModel.elapsedSeconds,
                    notes = viewModel.notes,
                    onNotesChange = viewModel::onNotesChange,
                    onSave = { viewModel.saveSession(onNavigateBack) },
                    onDiscard = { viewModel.discardSession(onNavigateBack) }
                )
            }
        }
    }
}

@Composable
private fun SetupContent(
    subjects: List<com.maryam.smartstudyplanner.data.local.entity.SubjectEntity>,
    topics: List<com.maryam.smartstudyplanner.data.local.entity.TopicEntity>,
    selectedSubjectId: Long?,
    selectedTopicId: Long?,
    subjectError: Boolean,
    onSubjectSelected: (Long) -> Unit,
    onTopicSelected: (Long?) -> Unit,
    onStart: () -> Unit
) {
    Text("Choose what you'll study", style = MaterialTheme.typography.titleMedium)

    DropdownField(
        label = "Subject",
        items = subjects,
        selectedItem = subjects.find { it.subjectId == selectedSubjectId },
        itemLabel = { it.name },
        isError = subjectError,
        onItemSelected = { onSubjectSelected(it.subjectId) }
    )

    if (topics.isNotEmpty()) {
        DropdownField(
            label = "Topic (optional)",
            items = topics,
            selectedItem = topics.find { it.topicId == selectedTopicId },
            itemLabel = { it.name },
            onItemSelected = { onTopicSelected(it.topicId) }
        )
    }

    Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
        Text("Start Session")
    }
}

@Composable
private fun RunningContent(elapsedSeconds: Long, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Session in progress", style = MaterialTheme.typography.titleMedium)
        Text(
            text = formatElapsedTime(elapsedSeconds),
            style = MaterialTheme.typography.titleLarge
        )
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) {
            Text("Finish Session")
        }
    }
}

@Composable
private fun SummaryContent(
    elapsedSeconds: Long,
    notes: String,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    onDiscard: () -> Unit
) {
    Text("Session Summary", style = MaterialTheme.typography.titleMedium)
    Text(
        text = "Duration: ${formatElapsedTime(elapsedSeconds)}",
        style = MaterialTheme.typography.bodyLarge
    )
    OutlinedTextField(
        value = notes,
        onValueChange = onNotesChange,
        label = { Text("Notes (optional)") },
        maxLines = 4,
        modifier = Modifier.fillMaxWidth()
    )
    Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
        Text("Save Session")
    }
    OutlinedButton(onClick = onDiscard, modifier = Modifier.fillMaxWidth()) {
        Text("Discard")
    }
}