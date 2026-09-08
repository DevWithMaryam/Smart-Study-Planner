package com.maryam.smartstudyplanner.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus
import com.maryam.smartstudyplanner.data.repository.TaskRepository
import com.maryam.smartstudyplanner.util.NotificationHelper
import com.maryam.smartstudyplanner.util.endOfToday
import com.maryam.smartstudyplanner.util.startOfToday
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

// @HiltWorker: Hilt ko batata hai ke yeh Worker bhi dependency injection
// use kar sakta hai (yahan TaskRepository). @AssistedInject + @Assisted
// zaroori hai kyunke Context aur WorkerParameters runtime pe WorkManager
// khud provide karta hai, Hilt nahi.
@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskRepository: TaskRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dueToday = taskRepository
                .getTasksForDateRange(startOfToday(), endOfToday())
                .first()
                .filter { it.status == TaskStatus.PENDING }

            val overdue = taskRepository.getOverdueTasks().first()

            if (dueToday.isNotEmpty() || overdue.isNotEmpty()) {
                NotificationHelper.showTaskReminder(
                    applicationContext,
                    dueToday.size,
                    overdue.size
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}