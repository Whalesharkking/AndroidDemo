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
import com.example.meinedemo.electronics.Electronic
import com.example.meinedemo.electronics.ElectronicData

@Composable
fun ElectronicInfoScreen(
    currentElectronic: Electronic, navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = currentElectronic.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        val text = currentElectronic.data?.let {
            it::class.members
                .filterIsInstance<kotlin.reflect.KProperty1<ElectronicData, *>>()
                .mapNotNull { prop -> prop.get(it)?.let { value -> "${prop.name}: $value" } }
                .joinToString(", ")
        } ?: ""
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
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