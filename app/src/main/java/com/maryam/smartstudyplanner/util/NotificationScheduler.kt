package com.maryam.smartstudyplanner.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maryam.smartstudyplanner.worker.TaskReminderWorker
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val WORK_NAME = "task_reminder_work"

    // Roz ek dafa (24 ghante baad) chalega. WorkManager ki minimum
    // periodic interval 15 minutes hai, lekin humein daily reminder
    // chahiye isliye 24 hours diya.
    fun scheduleDailyReminder(context: Context) {
        val request = PeriodicWorkRequestBuilder<TaskReminderWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    // TESTING ONLY: yeh function turant (bina wait kiye) worker chalata hai
    // taake hum notification manually test kar sakein. Testing ke baad
    // isay call karna band kar dena (ya function hi hata dena).
    fun scheduleImmediateTestRun(context: Context) {
        val request = OneTimeWorkRequestBuilder<TaskReminderWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}