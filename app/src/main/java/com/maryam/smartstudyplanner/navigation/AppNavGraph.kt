package com.maryam.smartstudyplanner.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maryam.smartstudyplanner.ui.calendar.CalendarScreen
import com.maryam.smartstudyplanner.ui.goals.GoalsScreen
import com.maryam.smartstudyplanner.ui.home.HomeScreen
import com.maryam.smartstudyplanner.ui.progress.ProgressScreen
import com.maryam.smartstudyplanner.ui.study.SessionHistoryScreen
import com.maryam.smartstudyplanner.ui.study.StudySessionScreen
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
                onTaskClick = { taskId -> navController.navigate(Screen.AddEditTask.createRoute(taskId)) },
                onStartSessionClick = { navController.navigate(Screen.StudySession.route) },
                onGoalsClick = { navController.navigate(Screen.Goals.route) }
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
            CalendarScreen(
                onTaskClick = { taskId -> navController.navigate(Screen.AddEditTask.createRoute(taskId)) }
            )
        }
        composable(Screen.Progress.route) {
            ProgressScreen()
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
        composable(Screen.StudySession.route) {
            StudySessionScreen(
                onNavigateBack = { navController.popBackStack() },
                onHistoryClick = { navController.navigate(Screen.SessionHistory.route) }
            )
        }
        composable(Screen.SessionHistory.route) {
            SessionHistoryScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Goals.route) {
            GoalsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}