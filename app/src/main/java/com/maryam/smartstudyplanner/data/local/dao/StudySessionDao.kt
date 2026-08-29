package com.maryam.smartstudyplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Update
    suspend fun updateSession(session: StudySessionEntity)

    @Delete
    suspend fun deleteSession(session: StudySessionEntity)

    @Query("SELECT * FROM study_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE subjectId = :subjectId ORDER BY date DESC")
    fun getSessionsForSubject(subjectId: Long): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE date BETWEEN :startOfDay AND :endOfDay ORDER BY date DESC")
    fun getSessionsForDateRange(startOfDay: Long, endOfDay: Long): Flow<List<StudySessionEntity>>

    @Query("SELECT COALESCE(SUM(durationMinutes), 0) FROM study_sessions WHERE date BETWEEN :startOfDay AND :endOfDay")
    fun getTotalDurationForDateRange(startOfDay: Long, endOfDay: Long): Flow<Long>
}