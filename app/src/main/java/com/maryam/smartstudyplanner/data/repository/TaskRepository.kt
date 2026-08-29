package com.maryam.smartstudyplanner.data.repository

import com.maryam.smartstudyplanner.data.local.dao.TaskDao
import com.maryam.smartstudyplanner.data.local.entity.TaskEntity
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getTasksByStatus(status: TaskStatus): Flow<List<TaskEntity>> =
        taskDao.getTasksByStatus(status)

    fun getTasksForSubject(subjectId: Long): Flow<List<TaskEntity>> =
        taskDao.getTasksForSubject(subjectId)

    fun getTaskById(taskId: Long): Flow<TaskEntity?> =
        taskDao.getTaskById(taskId)

    fun getOverdueTasks(): Flow<List<TaskEntity>> = taskDao.getOverdueTasks()

    fun getTasksForDateRange(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>> =
        taskDao.getTasksForDateRange(startOfDay, endOfDay)

    suspend fun addTask(task: TaskEntity): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)
}