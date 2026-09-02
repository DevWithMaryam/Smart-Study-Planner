package com.maryam.smartstudyplanner.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.repository.StudySessionRepository
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TaskRepository
import com.maryam.smartstudyplanner.util.dayOfMonth
import com.maryam.smartstudyplanner.util.endOfDay
import com.maryam.smartstudyplanner.util.endOfMonth
import com.maryam.smartstudyplanner.util.startOfDay
import com.maryam.smartstudyplanner.util.startOfMonth
import com.maryam.smartstudyplanner.util.startOfToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val studySessionRepository: StudySessionRepository,
    subjectRepository: SubjectRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(startOfToday())
    val selectedDate: StateFlow<Long> = _selectedDate

    private val _displayedMonthStart = MutableStateFlow(startOfMonth(startOfToday()))
    val displayedMonthStart: StateFlow<Long> = _displayedMonthStart

    val subjectNames: StateFlow<Map<Long, String>> = subjectRepository.getAllSubjects()
        .map { list -> list.associate { it.subjectId to it.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Displayed month ke andar jin dinon mein task/session hai, unke day-numbers ka set —
    // yeh grid pe dot marker dikhane ke liye use hota hai.
    val markedDays: StateFlow<Set<Int>> = _displayedMonthStart.flatMapLatest { monthStart ->
        val monthEnd = endOfMonth(monthStart)
        combine(
            taskRepository.getTasksForDateRange(monthStart, monthEnd),
            studySessionRepository.getSessionsForDateRange(monthStart, monthEnd)
        ) { tasks, sessions ->
            val days = mutableSetOf<Int>()
            tasks.forEach { task -> task.dueDate?.let { days.add(dayOfMonth(it)) } }
            sessions.forEach { session -> days.add(dayOfMonth(session.date)) }
            days
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val selectedDayTasks: StateFlow<List<TaskEntity>> = _selectedDate.flatMapLatest { date ->
        taskRepository.getTasksForDateRange(startOfDay(date), endOfDay(date))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val selectedDaySessions: StateFlow<List<StudySessionEntity>> = _selectedDate.flatMapLatest { date ->
        studySessionRepository.getSessionsForDateRange(startOfDay(date), endOfDay(date))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDate(date: Long) {
        _selectedDate.value = startOfDay(date)
    }

    fun goToPreviousMonth() {
        _displayedMonthStart.value =
            com.maryam.smartstudyplanner.util.addMonths(_displayedMonthStart.value, -1)
    }

    fun goToNextMonth() {
        _displayedMonthStart.value =
            com.maryam.smartstudyplanner.util.addMonths(_displayedMonthStart.value, 1)
    }
}