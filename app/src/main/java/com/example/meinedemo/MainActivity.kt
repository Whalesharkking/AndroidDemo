package com.example.meinedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.meinedemo.navigation.DemoApplicationScreens
import com.example.meinedemo.ui.screens.DetailScreen
import com.example.meinedemo.ui.screens.HomeScreen
import com.example.meinedemo.ui.screens.InfoScreen
import com.example.meinedemo.ui.theme.MeineDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeineDemoTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DemoNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = DemoApplicationScreens.Home.name,
        modifier = modifier
    ) {
        composable(route = DemoApplicationScreens.Home.name) {
            HomeScreen(navController)
        }

        composable(
            route = "${DemoApplicationScreens.Detail.name}/{senderText}",
            arguments = listOf(navArgument("senderText") { type = NavType.StringType })
        ) { backStackEntry ->
            val senderText = backStackEntry.arguments?.getString("senderText") ?: "error"
            DetailScreen(senderText, navController)
        }

        composable(
            route = "${DemoApplicationScreens.Info.name}/{senderText}/{items}",
            arguments = listOf(
                navArgument("senderText") { type = NavType.StringType },
                navArgument("items") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val senderText = backStackEntry.arguments?.getString("senderText") ?: "error"
            val items = backStackEntry.arguments?.getString("items") ?: "error"
            InfoScreen(senderText, items, navController)
        }
    }
}
