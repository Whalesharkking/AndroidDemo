package com.example.meinedemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import coil3.compose.AsyncImage
import com.example.meinedemo.bands.BandInfo

@Composable
fun BandInfoScreen(
    currentBand: BandInfo, navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = currentBand.name,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "${currentBand.homeCountry}, ${currentBand.foundingYear}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        AsyncImage(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            model = currentBand.bestOfCdCoverImageUrl,
            contentDescription = null
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