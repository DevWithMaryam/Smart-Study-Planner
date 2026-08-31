package com.maryam.smartstudyplanner.ui.subjects

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.local.entity.TopicEntity
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TopicRepository
import com.maryam.smartstudyplanner.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subjectRepository: SubjectRepository,
    private val topicRepository: TopicRepository
) : ViewModel() {

    // SavedStateHandle: Navigation Compose se jo argument pass hota hai
    // (yahan subjectId), woh Hilt automatically is handle mein daal deta hai.
    private val subjectId: Long =
        checkNotNull(savedStateHandle[Screen.SubjectDetails.ARG_SUBJECT_ID])

    val subject: StateFlow<SubjectEntity?> = subjectRepository.getSubjectById(subjectId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val topics: StateFlow<List<TopicEntity>> = topicRepository.getTopicsForSubject(subjectId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTopic(name: String, description: String) {
        viewModelScope.launch {
            topicRepository.addTopic(
                TopicEntity(subjectId = subjectId, name = name, description = description)
            )
        }
    }

    fun updateTopic(topic: TopicEntity) {
        viewModelScope.launch { topicRepository.updateTopic(topic) }
    }

    fun deleteTopic(topic: TopicEntity) {
        viewModelScope.launch { topicRepository.deleteTopic(topic) }
    }
}