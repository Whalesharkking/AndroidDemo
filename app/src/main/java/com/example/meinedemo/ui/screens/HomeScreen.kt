package com.example.meinedemo.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meinedemo.navigation.DemoApplicationScreens

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Text(
        text = "Welcome to the HomeScreen",
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleLarge,
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Button(onClick = {
                navHostController.navigate("${DemoApplicationScreens.Detail.name}/HomeScreen")
            }) {
                Text(text = "To DetailScreen")
            }
            val items = listOf("Apple", "Banana", "Cherry").joinToString(",")
            Button(onClick = {
                navHostController.navigate("${DemoApplicationScreens.Info.name}/HomeScreen/$items")
            }) {
                Text(text = "To InfoScreen")
            }
            Text(
                text = "With the button above, we can navigate to a new screen",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(end = 10.dp)
            )

        }
    }
}
