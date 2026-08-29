package com.maryam.smartstudyplanner.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class GoalStatus { ACTIVE, COMPLETED, EXPIRED }

@Entity(
    tableName = "study_goals",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["subjectId"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("subjectId")]
)
data class StudyGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val goalId: Long = 0,
    val subjectId: Long? = null,
    val title: String,
    val targetMinutes: Long,
    val startDate: Long,
    val endDate: Long,
    val status: GoalStatus = GoalStatus.ACTIVE
)