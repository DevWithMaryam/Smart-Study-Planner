package com.maryam.smartstudyplanner.navigation

// sealed class: yeh ek restricted class hierarchy define karti hai —
// yani Screen sirf inhi 5 types mein se koi ek ho sakta hai.
// Navigation routes ko type-safe tareeke se represent karne ke liye best hai.
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Subjects : Screen("subjects")
    data object Tasks : Screen("tasks")
    data object Calendar : Screen("calendar")
    data object Progress : Screen("progress")
}