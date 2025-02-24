package com.example.meinedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun DemoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost( // Definiert Screens wo man hin möchte
        navController = navController,
        startDestination = DemoApplicationScreens.Home.name,
        modifier = modifier
    ) {
        composable(route = DemoApplicationScreens.Home.name) {
            HomeScreen(
                "Home Screen", navController
            )
        }
        composable(
            route = "${DemoApplicationScreens.Detail.name}/{senderText}",
            arguments = listOf(
                navArgument("senderText") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val senderText = navBackStackEntry.arguments?.getString("senderText") ?: "error"
            DetailScreen(senderText = senderText)
        }
    }
}

@Composable
fun HomeScreen(
    name: String,
    navHostController: NavHostController,
) {
    Button(onClick = {
        navHostController.navigate("${DemoApplicationScreens.Detail.name}/HomeScreen")
    }) {
        Text(
            text = "$name Navigiere"
        )
    }
}

@Composable
fun DetailScreen(senderText: String) {
    Text("Welcome to DetailScreen from $senderText")
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
}