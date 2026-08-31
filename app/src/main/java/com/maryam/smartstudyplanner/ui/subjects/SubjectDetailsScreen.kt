package com.maryam.smartstudyplanner.ui.subjects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.maryam.smartstudyplanner.data.local.entity.TopicEntity
import com.maryam.smartstudyplanner.ui.components.ConfirmDeleteDialog
import com.maryam.smartstudyplanner.ui.subjects.components.AddEditTopicDialog
import com.maryam.smartstudyplanner.ui.subjects.components.TopicListItem
import androidx.compose.material3.ExperimentalMaterial3Api


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SubjectDetailsViewModel = hiltViewModel()
) {
    val subject by viewModel.subject.collectAsState()
    val topics by viewModel.topics.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingTopic by remember { mutableStateOf<TopicEntity?>(null) }
    var topicToDelete by remember { mutableStateOf<TopicEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subject?.name ?: "Subject") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingTopic = null
                showAddEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add topic")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            subject?.let { subj ->
                if (subj.description.isNotBlank()) {
                    Text(
                        text = subj.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Text(
                text = "Topics",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (topics.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No topics yet. Tap + to add one.", textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(topics, key = { it.topicId }) { topic ->
                        TopicListItem(
                            topic = topic,
                            onEditClick = {
                                editingTopic = topic
                                showAddEditDialog = true
                            },
                            onDeleteClick = { topicToDelete = topic }
                        )
                    }
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditTopicDialog(
            topic = editingTopic,
            onDismiss = { showAddEditDialog = false },
            onConfirm = { name, description ->
                val current = editingTopic
                if (current == null) {
                    viewModel.addTopic(name, description)
                } else {
                    viewModel.updateTopic(current.copy(name = name, description = description))
                }
                showAddEditDialog = false
            }
        )
    }

    topicToDelete?.let { topic ->
        ConfirmDeleteDialog(
            title = "Delete topic?",
            message = "This will remove \"${topic.name}\" from this subject.",
            onConfirm = {
                viewModel.deleteTopic(topic)
                topicToDelete = null
            },
            onDismiss = { topicToDelete = null }
        )
    }
}