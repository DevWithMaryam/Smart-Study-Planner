package com.maryam.smartstudyplanner.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maryam.smartstudyplanner.ui.subjects.SubjectDetailsScreen
import com.maryam.smartstudyplanner.ui.subjects.SubjectsScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Screen.Home.route) {
            PlaceholderScreen("Home — coming in Phase 5")
        }
        composable(Screen.Subjects.route) {
            SubjectsScreen(
                onSubjectClick = { subjectId ->
                    navController.navigate(Screen.SubjectDetails.createRoute(subjectId))
                }
            )
        }
        composable(Screen.Tasks.route) {
            PlaceholderScreen("Tasks — coming in Phase 4")
        }
        composable(Screen.Calendar.route) {
            PlaceholderScreen("Calendar — coming in Phase 8")
        }
        composable(Screen.Progress.route) {
            PlaceholderScreen("Progress — coming in Phase 10")
        }
        composable(
            route = Screen.SubjectDetails.route,
            arguments = listOf(
                navArgument(Screen.SubjectDetails.ARG_SUBJECT_ID) { type = NavType.LongType }
            )
        ) {
            SubjectDetailsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text)
    }
}