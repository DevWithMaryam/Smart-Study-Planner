package com.maryam.smartstudyplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maryam.smartstudyplanner.ui.home.HomeScreen
import com.maryam.smartstudyplanner.ui.subjects.SubjectDetailsScreen
import com.maryam.smartstudyplanner.ui.subjects.SubjectsScreen
import com.maryam.smartstudyplanner.ui.tasks.AddEditTaskScreen
import com.maryam.smartstudyplanner.ui.tasks.TasksScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddTaskClick = { navController.navigate(Screen.AddEditTask.createRoute()) },
                onTaskClick = { taskId -> navController.navigate(Screen.AddEditTask.createRoute(taskId)) }
            )
        }
        composable(Screen.Subjects.route) {
            SubjectsScreen(
                onSubjectClick = { subjectId ->
                    navController.navigate(Screen.SubjectDetails.createRoute(subjectId))
                }
            )
        }
        composable(Screen.Tasks.route) {
            TasksScreen(
                onAddTaskClick = { navController.navigate(Screen.AddEditTask.createRoute()) },
                onTaskClick = { taskId -> navController.navigate(Screen.AddEditTask.createRoute(taskId)) }
            )
        }
        composable(Screen.Calendar.route) {
            androidx.compose.material3.Text("Calendar — coming in Phase 8")
        }
        composable(Screen.Progress.route) {
            androidx.compose.material3.Text("Progress — coming in Phase 10")
        }
        composable(
            route = Screen.SubjectDetails.route,
            arguments = listOf(
                navArgument(Screen.SubjectDetails.ARG_SUBJECT_ID) { type = NavType.LongType }
            )
        ) {
            SubjectDetailsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.AddEditTask.route,
            arguments = listOf(
                navArgument(Screen.AddEditTask.ARG_TASK_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditTaskScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}