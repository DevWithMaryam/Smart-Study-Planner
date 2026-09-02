package com.maryam.smartstudyplanner.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import com.maryam.smartstudyplanner.data.repository.StudySessionRepository
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionHistoryViewModel @Inject constructor(
    studySessionRepository: StudySessionRepository,
    subjectRepository: SubjectRepository
) : ViewModel() {

    val sessions: StateFlow<List<StudySessionEntity>> = studySessionRepository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subjectNames: StateFlow<Map<Long, String>> = subjectRepository.getAllSubjects()
        .map { list -> list.associate { it.subjectId to it.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}