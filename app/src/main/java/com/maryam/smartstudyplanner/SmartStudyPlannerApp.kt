package com.maryam.smartstudyplanner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp: is annotation se Hilt poore app ke liye
// dependency injection container generate karta hai.
// Har Hilt project mein exactly ek Application class aisi honi chahiye.
@HiltAndroidApp
class SmartStudyPlannerApp : Application()