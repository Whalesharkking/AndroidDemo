package com.example.meinedemo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun UserScreen() {
    Column {
        var name by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier
                .padding(top = 16.dp),
            value = name,
            onValueChange = { newName ->
                name = newName
            },
            label = { Text("Name") }
        )
        var age by remember { mutableIntStateOf(0) }

        TextField(
            modifier = Modifier
                .padding(top = 16.dp),
            value = age.toString(),
            onValueChange = { newAge ->
                age = newAge.toIntOrNull() ?: 0
            },
            label = { Text("Alter") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        var isActive by remember { mutableStateOf(false) }
        Switch(
            checked = isActive,
            onCheckedChange = { isChecked ->
                isActive = isChecked
            }
        )
    }
}