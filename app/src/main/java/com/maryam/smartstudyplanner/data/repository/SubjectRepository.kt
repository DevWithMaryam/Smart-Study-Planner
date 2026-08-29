package com.maryam.smartstudyplanner.data.repository

import com.maryam.smartstudyplanner.data.local.dao.SubjectDao
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val subjectDao: SubjectDao
) {
    fun getAllSubjects(): Flow<List<SubjectEntity>> = subjectDao.getAllSubjects()

    fun getSubjectById(subjectId: Long): Flow<SubjectEntity?> =
        subjectDao.getSubjectById(subjectId)

    suspend fun addSubject(subject: SubjectEntity): Long =
        subjectDao.insertSubject(subject)

    suspend fun updateSubject(subject: SubjectEntity) =
        subjectDao.updateSubject(subject)

    suspend fun deleteSubject(subject: SubjectEntity) =
        subjectDao.deleteSubject(subject)
}