package com.maryam.smartstudyplanner.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TaskFilter { ALL, PENDING, COMPLETED, OVERDUE }
enum class TaskSort { DUE_DATE, PRIORITY, CREATED }

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    subjectRepository: SubjectRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(TaskFilter.ALL)
    val filter: StateFlow<TaskFilter> = _filter.asStateFlow()

    private val _sort = MutableStateFlow(TaskSort.DUE_DATE)
    val sort: StateFlow<TaskSort> = _sort.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // flatMapLatest: jab bhi filter badle, purani query cancel ho kar
    // nayi query start hoti hai — isse purana data leak nahi hota.
    private val rawTasks = _filter.flatMapLatest { filter ->
        when (filter) {
            TaskFilter.ALL -> taskRepository.getAllTasks()
            TaskFilter.PENDING -> taskRepository.getTasksByStatus(TaskStatus.PENDING)
            TaskFilter.COMPLETED -> taskRepository.getTasksByStatus(TaskStatus.COMPLETED)
            TaskFilter.OVERDUE -> taskRepository.getOverdueTasks()
        }
    }

    val subjectNames: StateFlow<Map<Long, String>> = subjectRepository.getAllSubjects()
        .map { list -> list.associate { it.subjectId to it.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val tasks: StateFlow<List<TaskEntity>> = combine(
        rawTasks, _searchQuery, _sort
    ) { taskList, query, sort ->
        val filtered = if (query.isBlank()) {
            taskList
        } else {
            taskList.filter { it.title.contains(query, ignoreCase = true) }
        }
        when (sort) {
            TaskSort.DUE_DATE -> filtered.sortedBy { it.dueDate ?: Long.MAX_VALUE }
            TaskSort.PRIORITY -> filtered.sortedByDescending { it.priority.ordinal }
            TaskSort.CREATED -> filtered.sortedByDescending { it.createdAt }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(value: TaskFilter) { _filter.value = value }
    fun setSort(value: TaskSort) { _sort.value = value }
    fun setSearchQuery(value: String) { _searchQuery.value = value }

    fun toggleCompletion(task: TaskEntity) {
        viewModelScope.launch {
            val updated = if (task.status == TaskStatus.PENDING) {
                task.copy(status = TaskStatus.COMPLETED, completedAt = System.currentTimeMillis())
            } else {
                task.copy(status = TaskStatus.PENDING, completedAt = null)
            }
            taskRepository.updateTask(updated)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { taskRepository.deleteTask(task) }
    }
}