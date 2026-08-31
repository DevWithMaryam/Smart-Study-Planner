package com.maryam.smartstudyplanner.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Subjects : Screen("subjects")
    data object Tasks : Screen("tasks")
    data object Calendar : Screen("calendar")
    data object Progress : Screen("progress")

    data object SubjectDetails : Screen("subject_details/{subjectId}") {
        const val ARG_SUBJECT_ID = "subjectId"
        fun createRoute(subjectId: Long) = "subject_details/$subjectId"
    }
}