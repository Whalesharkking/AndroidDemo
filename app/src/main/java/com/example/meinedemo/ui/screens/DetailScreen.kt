package com.example.meinedemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DetailScreen(senderText: String, navHostController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Texte gleichmäßig verteilen
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Start),
            text = "Oben Anfang"
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            text = "Oben Mitte"
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.End),
            text = "Mitte Ende"
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            text = "Unten Mitte"
        )
        Text(
            modifier = Modifier
                .align(Alignment.Start),
            text = "Unten Anfang"
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome to DetailScreen from $senderText",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                onClick = {
                    navHostController.popBackStack()
                }
            ) {
                Text("Go back")
            }
        }
    }
}