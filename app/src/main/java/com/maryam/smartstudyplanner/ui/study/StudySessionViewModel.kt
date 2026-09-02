package com.maryam.smartstudyplanner.ui.study

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.local.entity.TopicEntity
import com.maryam.smartstudyplanner.data.repository.StudySessionRepository
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SessionPhase { SETUP, RUNNING, FINISHED }

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StudySessionViewModel @Inject constructor(
    private val studySessionRepository: StudySessionRepository,
    subjectRepository: SubjectRepository,
    topicRepository: TopicRepository
) : ViewModel() {

    val subjects: StateFlow<List<SubjectEntity>> = subjectRepository.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubjectId = MutableStateFlow<Long?>(null)
    val selectedSubjectId: StateFlow<Long?> = _selectedSubjectId

    val topics: StateFlow<List<TopicEntity>> = _selectedSubjectId.flatMapLatest { subjectId ->
        if (subjectId == null) flowOf(emptyList()) else topicRepository.getTopicsForSubject(subjectId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var phase by mutableStateOf(SessionPhase.SETUP)
        private set
    var selectedTopicId by mutableStateOf<Long?>(null)
        private set
    var elapsedSeconds by mutableStateOf(0L)
        private set
    var notes by mutableStateOf("")
        private set
    var subjectError by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    fun onSubjectSelected(id: Long) {
        _selectedSubjectId.value = id
        selectedTopicId = null
        subjectError = false
    }

    fun onTopicSelected(id: Long?) {
        selectedTopicId = id
    }

    fun onNotesChange(value: String) {
        notes = value
    }

    fun startSession() {
        if (_selectedSubjectId.value == null) {
            subjectError = true
            return
        }
        phase = SessionPhase.RUNNING
        // Har second elapsedSeconds increment karta hai jab tak session running hai.
        // viewModelScope se launch hone ki wajah se ViewModel clear hote hi yeh
        // coroutine automatically cancel ho jata hai — memory leak nahi hota.
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    fun finishSession() {
        timerJob?.cancel()
        phase = SessionPhase.FINISHED
    }

    fun discardSession(onDone: () -> Unit) {
        timerJob?.cancel()
        resetState()
        onDone()
    }

    fun saveSession(onDone: () -> Unit) {
        val subjectId = _selectedSubjectId.value ?: return
        val durationMinutes = (elapsedSeconds / 60).coerceAtLeast(1)

        viewModelScope.launch {
            studySessionRepository.addSession(
                StudySessionEntity(
                    subjectId = subjectId,
                    topicId = selectedTopicId,
                    durationMinutes = durationMinutes,
                    notes = notes.trim().ifBlank { null }
                )
            )
            resetState()
            onDone()
        }
    }

    private fun resetState() {
        _selectedSubjectId.value = null
        selectedTopicId = null
        elapsedSeconds = 0L
        notes = ""
        phase = SessionPhase.SETUP
        subjectError = false
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}