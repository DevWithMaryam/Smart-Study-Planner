package com.maryam.smartstudyplanner.ui.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskPriority
import com.maryam.smartstudyplanner.data.local.entity.TopicEntity
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TaskRepository
import com.maryam.smartstudyplanner.data.repository.TopicRepository
import com.maryam.smartstudyplanner.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    subjectRepository: SubjectRepository,
    topicRepository: TopicRepository
) : ViewModel() {

    private val taskId: Long? =
        (savedStateHandle.get<Long>(Screen.AddEditTask.ARG_TASK_ID) ?: -1L)
            .takeIf { it != -1L }

    val isEditMode: Boolean = taskId != null

    val subjects: StateFlow<List<SubjectEntity>> = subjectRepository.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubjectId = MutableStateFlow<Long?>(null)
    val selectedSubjectId: StateFlow<Long?> = _selectedSubjectId

    val topics: StateFlow<List<TopicEntity>> = _selectedSubjectId.flatMapLatest { subjectId ->
        if (subjectId == null) flowOf(emptyList()) else topicRepository.getTopicsForSubject(subjectId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(TaskPriority.MEDIUM)
        private set
    var dueDate by mutableStateOf<Long?>(null)
        private set
    var selectedTopicId by mutableStateOf<Long?>(null)
        private set
    var titleError by mutableStateOf(false)
        private set
    var subjectError by mutableStateOf(false)
        private set

    init {
        taskId?.let { id ->
            viewModelScope.launch {
                val task = taskRepository.getTaskById(id).first()
                task?.let {
                    title = it.title
                    description = it.description
                    priority = it.priority
                    dueDate = it.dueDate
                    _selectedSubjectId.value = it.subjectId
                    selectedTopicId = it.topicId
                }
            }
        }
    }

    fun onTitleChange(value: String) { title = value; titleError = false }
    fun onDescriptionChange(value: String) { description = value }
    fun onPriorityChange(value: TaskPriority) { priority = value }
    fun onDueDateChange(value: Long?) { dueDate = value }
    fun onSubjectSelected(id: Long) {
        _selectedSubjectId.value = id
        selectedTopicId = null
        subjectError = false
    }
    fun onTopicSelected(id: Long?) { selectedTopicId = id }

    fun save(onSuccess: () -> Unit) {
        if (title.isBlank()) { titleError = true; return }
        val subjectId = _selectedSubjectId.value
        if (subjectId == null) { subjectError = true; return }

        viewModelScope.launch {
            if (isEditMode && taskId != null) {
                val existing = taskRepository.getTaskById(taskId).first()
                existing?.let {
                    taskRepository.updateTask(
                        it.copy(
                            title = title.trim(),
                            description = description.trim(),
                            subjectId = subjectId,
                            topicId = selectedTopicId,
                            priority = priority,
                            dueDate = dueDate
                        )
                    )
                }
            } else {
                taskRepository.addTask(
                    TaskEntity(
                        subjectId = subjectId,
                        topicId = selectedTopicId,
                        title = title.trim(),
                        description = description.trim(),
                        priority = priority,
                        dueDate = dueDate
                    )
                )
            }
            onSuccess()
        }
    }
}