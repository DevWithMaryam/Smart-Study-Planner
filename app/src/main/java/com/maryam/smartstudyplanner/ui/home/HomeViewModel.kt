package com.maryam.smartstudyplanner.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus
import com.maryam.smartstudyplanner.data.repository.StudyGoalRepository
import com.maryam.smartstudyplanner.data.repository.StudySessionRepository
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import com.maryam.smartstudyplanner.data.repository.TaskRepository
import com.maryam.smartstudyplanner.util.endOfToday
import com.maryam.smartstudyplanner.util.startOfToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

data class HomeUiState(
    val greeting: String = "",
    val todayTasks: List<TaskEntity> = emptyList(),
    val upcomingTasks: List<TaskEntity> = emptyList(),
    val subjectNames: Map<Long, String> = emptyMap(),
    val todayStudyMinutes: Long = 0,
    val activeGoal: StudyGoalEntity? = null,
    val completedTodayCount: Int = 0,
    val totalTodayCount: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    taskRepository: TaskRepository,
    studySessionRepository: StudySessionRepository,
    studyGoalRepository: StudyGoalRepository,
    subjectRepository: SubjectRepository
) : ViewModel() {

    private val todayStart = startOfToday()
    private val todayEnd = endOfToday()

    val uiState: StateFlow<HomeUiState> = combine(
        taskRepository.getAllTasks(),
        studySessionRepository.getTotalDurationForDateRange(todayStart, todayEnd),
        studyGoalRepository.getActiveGoals(),
        subjectRepository.getAllSubjects()
    ) { allTasks, studyMinutes, activeGoals, subjects ->

        val subjectNames = subjects.associate { it.subjectId to it.name }

        val dueTodayTasks = allTasks.filter { task ->
            task.dueDate != null && task.dueDate in todayStart..todayEnd
        }

        val todayTasks = dueTodayTasks
            .filter { it.status == TaskStatus.PENDING }
            .sortedBy { it.dueDate }

        val completedTodayCount = dueTodayTasks.count { it.status == TaskStatus.COMPLETED }

        val upcomingTasks = allTasks
            .filter { it.status == TaskStatus.PENDING && it.dueDate != null && it.dueDate > todayEnd }
            .sortedBy { it.dueDate }
            .take(5)

        HomeUiState(
            greeting = greetingForNow(),
            todayTasks = todayTasks,
            upcomingTasks = upcomingTasks,
            subjectNames = subjectNames,
            todayStudyMinutes = studyMinutes,
            activeGoal = activeGoals.firstOrNull(),
            completedTodayCount = completedTodayCount,
            totalTodayCount = dueTodayTasks.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    private fun greetingForNow(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}