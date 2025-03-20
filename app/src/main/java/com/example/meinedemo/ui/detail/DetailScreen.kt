package com.example.meinedemo.ui.detail

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
import com.example.meinedemo.ui.detail.TextItem


@Composable
fun DetailScreen(senderText: String, navHostController: NavHostController) {
    val textItems = listOf(
        TextItem("Oben Anfang", Alignment.Start, true),
        TextItem("Oben Mitte", Alignment.CenterHorizontally, true),
        TextItem("Mitte Ende", Alignment.End, true),
        TextItem("Unten Mitte", Alignment.CenterHorizontally, true),
        TextItem("Unten Anfang", Alignment.Start, false)
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        textItems.forEach { item ->
            Text(
                modifier = Modifier
                    .then(
                        if (item.hasWeight)
                            Modifier.weight(1f)
                        else
                            Modifier)
                    .align(item.alignment),
                text = item.text
            )
        }

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