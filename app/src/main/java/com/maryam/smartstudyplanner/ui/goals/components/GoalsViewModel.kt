package com.maryam.smartstudyplanner.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.repository.StudyGoalRepository
import com.maryam.smartstudyplanner.data.repository.StudySessionRepository
import com.maryam.smartstudyplanner.data.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalWithProgress(
    val goal: StudyGoalEntity,
    val progressMinutes: Long,
    val isExpired: Boolean
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val studyGoalRepository: StudyGoalRepository,
    private val studySessionRepository: StudySessionRepository,
    subjectRepository: SubjectRepository
) : ViewModel() {

    val subjects: StateFlow<List<SubjectEntity>> = subjectRepository.getAllSubjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Har goal ke apne date-range ki progress alag se load karni hai,
    // isliye hum saare goals fetch karke, unke liye ek-ek "duration in range" query
    // suspend function se manually calculate kar rahe hain (loadProgress() se).
    private val _goalsWithProgress = MutableStateFlow<List<GoalWithProgress>>(emptyList())
    val goalsWithProgress: StateFlow<List<GoalWithProgress>> = _goalsWithProgress

    init {
        viewModelScope.launch {
            studyGoalRepository.getAllGoals().collect { goals ->
                val now = System.currentTimeMillis()
                val result = goals.map { goal ->
                    val minutes = studySessionRepository
                        .getTotalDurationForDateRange(goal.startDate, goal.endDate)
                        .first()
                    GoalWithProgress(
                        goal = goal,
                        progressMinutes = minutes,
                        isExpired = goal.endDate < now
                    )
                }
                _goalsWithProgress.value = result
            }
        }
    }

    fun addGoal(title: String, targetMinutes: Long, subjectId: Long?, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            studyGoalRepository.addGoal(
                StudyGoalEntity(
                    subjectId = subjectId,
                    title = title,
                    targetMinutes = targetMinutes,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }
    }

    fun updateGoal(goal: StudyGoalEntity) {
        viewModelScope.launch { studyGoalRepository.updateGoal(goal) }
    }

    fun deleteGoal(goal: StudyGoalEntity) {
        viewModelScope.launch { studyGoalRepository.deleteGoal(goal) }
    }
}