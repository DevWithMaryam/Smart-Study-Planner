package com.maryam.smartstudyplanner.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.maryam.smartstudyplanner.data.local.datastore.SettingsKeys
import com.maryam.smartstudyplanner.data.local.datastore.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        val stored = prefs[SettingsKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        // valueOf() crash kar sakta hai agar stored value corrupt ho —
        // isliye runCatching se safely fallback karte hain SYSTEM pe.
        runCatching { ThemeMode.valueOf(stored) }.getOrDefault(ThemeMode.SYSTEM)
    }

    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[SettingsKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val dailyGoalMinutes: Flow<Int> = dataStore.data.map { prefs ->
        prefs[SettingsKeys.DAILY_GOAL_MINUTES] ?: 60
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { prefs -> prefs[SettingsKeys.THEME_MODE] = mode.name }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[SettingsKeys.NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setDailyGoalMinutes(minutes: Int) {
        dataStore.edit { prefs -> prefs[SettingsKeys.DAILY_GOAL_MINUTES] = minutes }
    }
}