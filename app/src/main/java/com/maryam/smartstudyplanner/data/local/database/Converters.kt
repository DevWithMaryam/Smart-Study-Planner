package com.maryam.smartstudyplanner.data.local.database

import androidx.room.TypeConverter
import com.maryam.smartstudyplanner.data.local.entity.GoalStatus
import com.maryam.smartstudyplanner.data.local.entity.TaskPriority
import com.maryam.smartstudyplanner.data.local.entity.TaskStatus

class Converters {

    @TypeConverter
    fun fromTaskPriority(value: TaskPriority): String = value.name

    @TypeConverter
    fun toTaskPriority(value: String): TaskPriority = TaskPriority.valueOf(value)

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)

    @TypeConverter
    fun fromGoalStatus(value: GoalStatus): String = value.name

    @TypeConverter
    fun toGoalStatus(value: String): GoalStatus = GoalStatus.valueOf(value)
}