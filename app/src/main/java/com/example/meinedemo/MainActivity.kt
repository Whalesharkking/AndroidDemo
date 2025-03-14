package com.example.meinedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.meinedemo.bands.BandsViewModel
import com.example.meinedemo.electronics.ElectronicsViewModel
import com.example.meinedemo.navigation.DemoApplicationScreen
import com.example.meinedemo.ui.screens.BandInfoScreen
import com.example.meinedemo.ui.screens.DetailScreen
import com.example.meinedemo.ui.screens.ElectronicInfoScreen
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
                        modifier = Modifier.padding(innerPadding),
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
    NavHost(
        navController = navController,
        startDestination = DemoApplicationScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = DemoApplicationScreen.Home.name) {
            HomeScreen(navController, BandsViewModel(), ElectronicsViewModel())
        }

        composable(
            route = "${DemoApplicationScreen.Detail.name}/{senderText}",
            arguments = listOf(navArgument("senderText") { type = NavType.StringType })
        ) { backStackEntry ->
            val senderText = backStackEntry.arguments?.getString("senderText") ?: "error"
            DetailScreen(senderText, navController)
        }

        composable(
            route = "${DemoApplicationScreen.Info.name}/{senderText}/{items}",
            arguments = listOf(
                navArgument("senderText") { type = NavType.StringType },
                navArgument("items") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val senderText = backStackEntry.arguments?.getString("senderText") ?: "error"
            val items = backStackEntry.arguments?.getString("items") ?: "error"
            InfoScreen(senderText, items, navController)
        }
        composable(
            route = "${DemoApplicationScreen.BandInfo.name}/{bandCode}",
            arguments = listOf(navArgument("bandCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val bandCode = backStackEntry.arguments?.getString("bandCode") ?: "unknown"
            val viewModel = BandsViewModel()
            viewModel.requestBandInfoFromServer(bandCode)
            val bandInfoState = viewModel.currentBand.collectAsState(initial = null)
            bandInfoState.value?.let { bandInfo ->
                BandInfoScreen(bandInfo, navController)
            }
        }
        composable(
            route = "${DemoApplicationScreen.ElectronicInfo.name}/{electronicsCode}",
            arguments = listOf(navArgument("electronicsCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val electronicsCode =
                backStackEntry.arguments?.getString("electronicsCode") ?: "unknown"
            val viewModel = ElectronicsViewModel()
            viewModel.requestDetailsOfElectronic(electronicsCode)
            val electronicInfoState = viewModel.currentElectronic.collectAsState(initial = null)
            electronicInfoState.value?.let { electronicInfo ->
                ElectronicInfoScreen(electronicInfo, navController)
            }
        }
    }
}
