package com.example.meinedemo.ui.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.meinedemo.navigation.DemoApplicationScreen

@Composable
fun InfoScreen(senderText: String, items: String, navHostController: NavHostController) {
    val list = items.split(",")
    var selectedOption by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { navHostController.navigate(DemoApplicationScreen.Home.name) }) {
                Text(text = "To HomeScreen")
            }

            Button(onClick = { expanded = true }) {
                Text("Wähle eine Option")
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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

            Text(text = "Selected option: $selectedOption")
        }
    }
}
