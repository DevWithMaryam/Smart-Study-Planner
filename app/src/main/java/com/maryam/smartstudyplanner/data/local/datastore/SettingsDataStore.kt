package com.maryam.smartstudyplanner.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
}

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}