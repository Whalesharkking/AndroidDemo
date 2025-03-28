package com.example.meinedemo.ui.music

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.meinedemo.service.MusicPlayerConnection
import com.example.meinedemo.service.MusicPlayerService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MusicScreen() {
    val context = LocalContext.current
    val intent = remember { Intent(context, MusicPlayerService::class.java) }
    val serviceConnection = remember { MusicPlayerConnection() }
    val notificationPermissionState =
        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    val songHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RequestPermissionButton(notificationPermissionState)
        StartServiceButton(context, intent, serviceConnection, notificationPermissionState)
        StopServiceButton(context, serviceConnection)
        PlayNextSongButton(serviceConnection, songHistory, context)
        SongHistoryList(songHistory)
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionButton(permissionState: PermissionState) {
    Button(
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionState.launchPermissionRequest()
            }
        }
    ) {
        Text("Request permission")
    }
}

@OptIn(ExperimentalPermissionsApi::class)

@Composable
private fun StartServiceButton(
    context: Context,
    intent: Intent,
    serviceConnection: MusicPlayerConnection,
    permissionState: PermissionState
) {
    Button(
        onClick = {
            if (permissionState.status.isGranted || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            } else {
                Toast.makeText(context, "Notification permission required", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    ) {
        Text("Start service")
    }
}

@Composable
private fun StopServiceButton(
    context: Context,
    serviceConnection: MusicPlayerConnection
) {
    Button(
        onClick = {
            if (serviceConnection.isConnected()) {
                context.unbindService(serviceConnection)
                serviceConnection.clear()
            } else {
                Toast.makeText(context, "Service ist nicht verbunden", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text("Stop service")
    }
}

@Composable
private fun PlayNextSongButton(
    serviceConnection: MusicPlayerConnection,
    songHistory: MutableList<String>,
    context: Context
) {
    Button(
        onClick = {
            val title = serviceConnection.getMusicPlayerApi()?.playNextSong()
            if (title != null) {
                songHistory.add(title)
                Toast.makeText(context, "Playing: $title", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text("Play next song")
    }
}

@Composable
private fun SongHistoryList(songHistory: List<String>) {
    Text("History:")
    songHistory.forEach {
        Text(it)
    }
}

