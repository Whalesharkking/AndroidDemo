package com.example.meinedemo.ui.screens

import android.text.Layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.meinedemo.navigation.DemoApplicationScreens

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        }
    }
}
