package com.maryam.smartstudyplanner.di

import android.content.Context
import androidx.room.Room
import com.maryam.smartstudyplanner.data.local.dao.StudyGoalDao
import com.maryam.smartstudyplanner.data.local.dao.StudySessionDao
import com.maryam.smartstudyplanner.data.local.dao.SubjectDao
import com.maryam.smartstudyplanner.data.local.dao.TaskDao
import com.maryam.smartstudyplanner.data.local.dao.TopicDao
import com.maryam.smartstudyplanner.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_study_planner.db"
        ).build()
    }

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao = database.subjectDao()

    @Provides
    fun provideTopicDao(database: AppDatabase): TopicDao = database.topicDao()

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides
    fun provideStudySessionDao(database: AppDatabase): StudySessionDao = database.studySessionDao()

    @Provides
    fun provideStudyGoalDao(database: AppDatabase): StudyGoalDao = database.studyGoalDao()
}