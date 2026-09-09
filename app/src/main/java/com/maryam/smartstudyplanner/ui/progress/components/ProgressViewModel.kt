package com.maryam.smartstudyplanner.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus
import com.maryam.smartstudyplanner.data.repository.StudyGoalRepository
import com.maryam.smartstudyplanner.data.repository.StudySessionRepository
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TaskRepository
import com.maryam.smartstudyplanner.ui.progress.components.BarChartEntry
import com.maryam.smartstudyplanner.util.endOfToday
import com.maryam.smartstudyplanner.util.shortDayLabel
import com.maryam.smartstudyplanner.util.startOfDay
import com.maryam.smartstudyplanner.util.startOfDaysAgo
import com.maryam.smartstudyplanner.util.startOfMonth
import com.maryam.smartstudyplanner.util.endOfMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubjectProgressItem(
    val subjectName: String,
    val minutes: Long,
    val fraction: Float
)

data class ProgressUiState(
    val weeklyChart: List<BarChartEntry> = emptyList(),
    val monthlyTotalMinutes: Long = 0,
    val subjectProgress: List<SubjectProgressItem> = emptyList(),
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val overdueTasks: Int = 0,
    val activeGoalsCount: Int = 0
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val studySessionRepository: StudySessionRepository,
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val studyGoalRepository: StudyGoalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState

    init {
        loadWeeklyAndTaskData()
        loadMonthlyAndSubjectData()
    }

    private fun loadWeeklyAndTaskData() {
        viewModelScope.launch {
            combine(
                studySessionRepository.getSessionsForDateRange(startOfDaysAgo(6), endOfToday()),
                taskRepository.getAllTasks(),
                studyGoalRepository.getActiveGoals()
            ) { sessions, tasks, activeGoals ->

                // Pichle 7 din ke liye, har din ka total minutes group karte hain.
                val dayTotals = sessions.groupBy { startOfDay(it.date) }
                    .mapValues { entry -> entry.value.sumOf { it.durationMinutes } }

                val weeklyChart = (6 downTo 0).map { daysAgo ->
                    val dayStart = startOfDaysAgo(daysAgo)
                    BarChartEntry(
                        label = shortDayLabel(dayStart),
                        minutes = dayTotals[dayStart] ?: 0L
                    )
                }

                val completed = tasks.count { it.status == TaskStatus.COMPLETED }
                val pending = tasks.count { it.status == TaskStatus.PENDING }
                val overdue = tasks.count {
                    it.status == TaskStatus.PENDING &&
                            it.dueDate != null &&
                            it.dueDate < System.currentTimeMillis()
                }

                _uiState.value = _uiState.value.copy(
                    weeklyChart = weeklyChart,
                    completedTasks = completed,
                    pendingTasks = pending,
                    overdueTasks = overdue,
                    activeGoalsCount = activeGoals.size
                )
            }.collect {}
        }
    }

    private fun loadMonthlyAndSubjectData() {
        viewModelScope.launch {
            combine(
                studySessionRepository.getTotalDurationForDateRange(
                    startOfMonth(System.currentTimeMillis()),
                    endOfMonth(System.currentTimeMillis())
                ),
                studySessionRepository.getSubjectWiseDuration(),
                subjectRepository.getAllSubjects()
            ) { monthlyTotal, subjectDurations, subjects ->

                val subjectNames = subjects.associate { it.subjectId to it.name }
                val grandTotal = subjectDurations.sumOf { it.totalMinutes }.coerceAtLeast(1L)

                val subjectProgress = subjectDurations
                    .sortedByDescending { it.totalMinutes }
                    .map { sd ->
                        SubjectProgressItem(
                            subjectName = subjectNames[sd.subjectId] ?: "Unknown",
                            minutes = sd.totalMinutes,
                            fraction = sd.totalMinutes.toFloat() / grandTotal.toFloat()
                        )
                    }

                _uiState.value = _uiState.value.copy(
                    monthlyTotalMinutes = monthlyTotal,
                    subjectProgress = subjectProgress
                )
            }.collect {}
        }
    }
}