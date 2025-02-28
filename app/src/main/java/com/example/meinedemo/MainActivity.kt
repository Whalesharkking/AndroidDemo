package com.example.meinedemo

import android.os.Bundle
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        composable(
            route = DemoApplicationScreens.Home.name,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) },
            popEnterTransition = { fadeIn(animationSpec = tween(500)) },
            popExitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            HomeScreen(navController)
        }

        composable(
            route = "${DemoApplicationScreens.Detail.name}/{senderText}",
            arguments = listOf(navArgument("senderText") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) }
        ) { backStackEntry ->
            val senderText = backStackEntry.arguments?.getString("senderText") ?: "error"
            DetailScreen(senderText, navController)
        }

        composable(
            route = "${DemoApplicationScreens.Info.name}/{senderText}/{items}",
            arguments = listOf(
                navArgument("senderText") { type = NavType.StringType },
                navArgument("items") { type = NavType.StringType }
            ),
            enterTransition = { scaleIn(initialScale = 0.8f, animationSpec = tween(500)) },
            exitTransition = { scaleOut(targetScale = 1.2f, animationSpec = tween(500)) },
            popEnterTransition = { fadeIn(animationSpec = tween(500)) },
            popExitTransition = { fadeOut(animationSpec = tween(500)) }
        ) { backStackEntry ->
            val senderText = backStackEntry.arguments?.getString("senderText") ?: "error"
            val items = backStackEntry.arguments?.getString("items") ?: "error"
            InfoScreen(senderText, items, navController)
        }
    }
}


@Composable
fun HomeScreen(
    navHostController: NavHostController,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            navHostController.navigate("${DemoApplicationScreens.Detail.name}/HomeScreen")
        }) {
            Text(
                text = "To DetailScreen"
            )
        }
    }
}

@Composable
fun DetailScreen(senderText: String, navHostController: NavHostController) {
    val items = listOf("Apple", "Banana", "Cherry").joinToString(",");
    Text("Welcome to DetailScreen from $senderText")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            navHostController.navigate("${DemoApplicationScreens.Info.name}/DetailScreen/$items")
        }) {
            Text(
                text = "To InfoScreen"
            )
        }
    }
}

@Composable
fun InfoScreen(
    senderText: String,
    items: String,
    navHostController: NavHostController
) {
    val list = items.split(",")
    var selectedOption by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(true) }
    Text("Welcome to InfoScreen from $senderText")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                navHostController.navigate(DemoApplicationScreens.Home.name)
            }) {
                Text(
                    text = "To HomeScreen"
                )
            }

            Button(onClick = { expanded = true }) {
                Text("Wähle eine Option")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                list.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }

            Text(
                text = "Selected option: $selectedOption"
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MeineDemoTheme {
//        HomeScreen("Android")
//    }
//}

enum class DemoApplicationScreens {
    Home,
    Detail,
    Info,
}