package com.maryam.smartstudyplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: StudyGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: StudyGoalEntity)

    @Delete
    suspend fun deleteGoal(goal: StudyGoalEntity)

    @Query("SELECT * FROM study_goals ORDER BY startDate DESC")
    fun getAllGoals(): Flow<List<StudyGoalEntity>>

    @Query("SELECT * FROM study_goals WHERE status = 'ACTIVE' ORDER BY startDate DESC")
    fun getActiveGoals(): Flow<List<StudyGoalEntity>>

    @Query("SELECT * FROM study_goals WHERE subjectId = :subjectId ORDER BY startDate DESC")
    fun getGoalsForSubject(subjectId: Long): Flow<List<StudyGoalEntity>>
}