package com.maryam.smartstudyplanner.ui.study

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maryam.smartstudyplanner.ui.study.components.SessionListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionHistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: SessionHistoryViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessions.collectAsState()
    val subjectNames by viewModel.subjectNames.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Session History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No study sessions recorded yet.", textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(sessions, key = { it.sessionId }) { session ->
                    SessionListItem(
                        session = session,
                        subjectName = subjectNames[session.subjectId] ?: ""
                    )
                }
            }
        }
    }
}