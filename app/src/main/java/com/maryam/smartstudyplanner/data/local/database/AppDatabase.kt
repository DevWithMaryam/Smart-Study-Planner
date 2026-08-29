package com.maryam.smartstudyplanner.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maryam.smartstudyplanner.data.local.dao.StudyGoalDao
import com.maryam.smartstudyplanner.data.local.dao.StudySessionDao
import com.maryam.smartstudyplanner.data.local.dao.SubjectDao
import com.maryam.smartstudyplanner.data.local.dao.TaskDao
import com.maryam.smartstudyplanner.data.local.dao.TopicDao
import com.maryam.smartstudyplanner.data.local.entity.StudyGoalEntity
import com.maryam.smartstudyplanner.data.local.entity.StudySessionEntity
import com.maryam.smartstudyplanner.data.local.entity.SubjectEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.local.entity.TopicEntity

@Database(
    entities = [
        SubjectEntity::class,
        TopicEntity::class,
        TaskEntity::class,
        StudySessionEntity::class,
        StudyGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun topicDao(): TopicDao
    abstract fun taskDao(): TaskDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun studyGoalDao(): StudyGoalDao
}