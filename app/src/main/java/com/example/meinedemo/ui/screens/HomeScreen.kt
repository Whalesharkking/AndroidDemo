package com.example.meinedemo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meinedemo.navigation.DemoApplicationScreen
import com.example.meinedemo.ui.bands.BandsViewModel

@Composable
fun HomeScreen(navHostController: NavHostController, bandsViewModel: BandsViewModel) {

    Column {
        Text(
            text = "Welcome to the HomeScreen!!!",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {
                    navHostController.navigate("${DemoApplicationScreen.Detail.name}/HomeScreen")
                }) {
                    Text(text = "To DetailScreen")
                }
                val items = listOf("Apple", "Banana", "Cherry").joinToString(",")
                Button(onClick = {
                    navHostController.navigate("${DemoApplicationScreen.Info.name}/HomeScreen/$items")
                }) {
                    Text(text = "To InfoScreen")
                }
                val bands = bandsViewModel.bandsFlow.collectAsState()

                Button(onClick = {
                    bandsViewModel.requestBandCodesFromServer()
                }) {
                    Text(text = "Load Bands")
                }
                LazyColumn {
                    items(bands.value) { band ->  // Hier die richtige Funktion nutzen
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    navHostController.navigate(
                                        route = "${DemoApplicationScreen.BandInfo.name}/${band.code}"
                                    )
                                },
                            text = band.name
                        )
                    }
                }

            }
        }
    }
}
