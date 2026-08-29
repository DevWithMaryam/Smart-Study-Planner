package com.maryam.smartstudyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.maryam.smartstudyplanner.navigation.AppNavGraph
import com.maryam.smartstudyplanner.ui.theme.SmartStudyPlannerTheme
import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint: Hilt ko batata hai ke is Activity mein
// dependencies inject ho sakti hain. Har Activity/Fragment jahan
// Hilt use karna ho, waha yeh annotation lagana zaroori hai.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartStudyApp()
        }
    }
}

@Composable
fun SmartStudyApp() {
    SmartStudyPlannerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}