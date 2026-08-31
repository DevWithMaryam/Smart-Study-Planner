package com.maryam.smartstudyplanner.ui.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectsViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    // stateIn: Flow ko StateFlow mein convert karta hai, taake Compose
    // ise direct observe kar sake. WhileSubscribed(5000) ka matlab hai
    // ke UI 5 second tak background mein jaye to bhi collection zinda rehti hai
    // (jaise screen rotate ya quick navigation ke waqt data dobara load na ho).
    val subjects: StateFlow<List<SubjectEntity>> = subjectRepository.getAllSubjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addSubject(name: String, description: String) {
        viewModelScope.launch {
            subjectRepository.addSubject(SubjectEntity(name = name, description = description))
        }
    }

    fun updateSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            subjectRepository.updateSubject(subject)
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            subjectRepository.deleteSubject(subject)
        }
    }
}