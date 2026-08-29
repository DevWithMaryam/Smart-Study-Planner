package com.maryam.smartstudyplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["subjectId"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["topicId"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("subjectId"), Index("topicId")]
)
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0,
    val subjectId: Long,
    val topicId: Long? = null,
    val durationMinutes: Long,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
)