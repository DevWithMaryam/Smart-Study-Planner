package com.maryam.smartstudyplanner.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Icons.Default.Home),
    BottomNavItem(Screen.Subjects, "Subjects", Icons.Default.List),
    BottomNavItem(Screen.Tasks, "Tasks", Icons.Default.CheckCircle),
    BottomNavItem(Screen.Calendar, "Calendar", Icons.Default.DateRange),
    BottomNavItem(Screen.Progress, "Progress", Icons.Default.Star)
)