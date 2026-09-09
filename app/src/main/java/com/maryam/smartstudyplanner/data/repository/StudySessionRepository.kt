package com.maryam.smartstudyplanner.data.repository

import com.maryam.smartstudyplanner.data.local.dao.StudySessionDao
import com.maryam.smartstudyplanner.data.local.dao.SubjectDuration
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudySessionRepository @Inject constructor(
    private val studySessionDao: StudySessionDao
) {
    fun getAllSessions(): Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()

    fun getSessionsForSubject(subjectId: Long): Flow<List<StudySessionEntity>> =
        studySessionDao.getSessionsForSubject(subjectId)

    fun getSessionsForDateRange(startOfDay: Long, endOfDay: Long): Flow<List<StudySessionEntity>> =
        studySessionDao.getSessionsForDateRange(startOfDay, endOfDay)

    fun getTotalDurationForDateRange(startOfDay: Long, endOfDay: Long): Flow<Long> =
        studySessionDao.getTotalDurationForDateRange(startOfDay, endOfDay)

    fun getSubjectWiseDuration(): Flow<List<SubjectDuration>> =
        studySessionDao.getSubjectWiseDuration()

    suspend fun addSession(session: StudySessionEntity): Long =
        studySessionDao.insertSession(session)

    suspend fun updateSession(session: StudySessionEntity) =
        studySessionDao.updateSession(session)

    suspend fun deleteSession(session: StudySessionEntity) =
        studySessionDao.deleteSession(session)
}