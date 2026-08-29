package com.maryam.smartstudyplanner.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Screen.Home.route) { Text("Home", modifier = Modifier.padding(16.dp)) }
        composable(Screen.Subjects.route) { Text("Subjects") }
        composable(Screen.Tasks.route) { Text("Tasks") }
        composable(Screen.Calendar.route) { Text("Calendar") }
        composable(Screen.Progress.route) { Text("Progress") }
    }
}