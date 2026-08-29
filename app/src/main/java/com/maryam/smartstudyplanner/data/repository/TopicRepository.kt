package com.maryam.smartstudyplanner.data.repository

import com.maryam.smartstudyplanner.data.local.dao.TopicDao
import com.maryam.smartstudyplanner.data.local.entity.TopicEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopicRepository @Inject constructor(
    private val topicDao: TopicDao
) {
    fun getTopicsForSubject(subjectId: Long): Flow<List<TopicEntity>> =
        topicDao.getTopicsForSubject(subjectId)

    fun getTopicById(topicId: Long): Flow<TopicEntity?> =
        topicDao.getTopicById(topicId)

    suspend fun addTopic(topic: TopicEntity): Long =
        topicDao.insertTopic(topic)

    suspend fun updateTopic(topic: TopicEntity) =
        topicDao.updateTopic(topic)

    suspend fun deleteTopic(topic: TopicEntity) =
        topicDao.deleteTopic(topic)
}