package com.maryam.smartstudyplanner.data.repository

import com.maryam.smartstudyplanner.data.local.dao.StudyGoalDao
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudyGoalRepository @Inject constructor(
    private val studyGoalDao: StudyGoalDao
) {
    fun getAllGoals(): Flow<List<StudyGoalEntity>> = studyGoalDao.getAllGoals()

    fun getActiveGoals(): Flow<List<StudyGoalEntity>> = studyGoalDao.getActiveGoals()

    fun getGoalsForSubject(subjectId: Long): Flow<List<StudyGoalEntity>> =
        studyGoalDao.getGoalsForSubject(subjectId)

    suspend fun addGoal(goal: StudyGoalEntity): Long = studyGoalDao.insertGoal(goal)

    suspend fun updateGoal(goal: StudyGoalEntity) = studyGoalDao.updateGoal(goal)

    suspend fun deleteGoal(goal: StudyGoalEntity) = studyGoalDao.deleteGoal(goal)
}