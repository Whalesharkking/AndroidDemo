package com.example.meinedemo.ui.music

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.meinedemo.service.MusicPlayerService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MusicScreen() {
    LocalContext.current
    val notificationPermissionState =
        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Button für Berechtigungsanfrage
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        ) {
            Text(text = "Request permission")
        }

        val context = LocalContext.current

        // Button zum Starten des Services
        Button(
            onClick = {
                if (notificationPermissionState.status.isGranted || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    val intent = Intent(context, MusicPlayerService::class.java)
                    context.startForegroundService(intent)
                } else {
                    Toast.makeText(context, "Notification permission required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        ) {
            Text(text = "Start service")
        }

        // Button zum Stoppen des Services
        Button(
            onClick = {
                val intent = Intent(context, MusicPlayerService::class.java)
                context.stopService(intent)
            }
        ) {
            Text(text = "Stop service")
        }
    }
}

