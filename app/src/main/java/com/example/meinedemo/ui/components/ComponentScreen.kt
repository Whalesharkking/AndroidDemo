package com.example.meinedemo.ui.components

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.lifecycle.compose.LifecycleStartEffect
import com.example.meinedemo.components.BroadcastActions
import com.example.meinedemo.components.ContactLoader
import com.example.meinedemo.components.MyLocalBroadcastReceiver
import com.example.meinedemo.components.SmsLoader
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun BroadcastWidget() {
    val context = LocalContext.current

    // Lifecycle-aware Registrierung des lokalen Receivers
    LifecycleStartEffect(Unit) {
        val receiver = MyLocalBroadcastReceiver()

        // Lokalen BroadcastReceiver registrieren (nur intern)
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(BroadcastActions.LOCAL_ACTION),
            RECEIVER_NOT_EXPORTED
        )

        // Bei STOP automatisch wieder abmelden
        onStopOrDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeadLine("Broadcasts")

            SendBroadcast(context, BroadcastActions.MANIFEST_ACTION, "Manifest")
            SendBroadcast(context, BroadcastActions.LOCAL_ACTION, "Local")

            HeadLine("Content Provider")

            SmsWidget()
            ContactWidget()
        }
    }

}

@Composable
fun SmsWidget() {
    LoadWidget(
        permission = android.Manifest.permission.READ_SMS,
        buttonText = "Read SMS",
        loadData = { SmsLoader.readSms(it) }
    )
}

@Composable
fun ContactWidget() {
    LoadWidget(
        permission = android.Manifest.permission.READ_CONTACTS,
        buttonText = "Read Contacts",
        loadData = { ContactLoader.readContacts(it) }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadWidget(
    permission: String,
    buttonText: String,
    loadData: (Context) -> List<DisplayEntry>
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission)
    val entries = remember { mutableStateListOf<DisplayEntry>() }

    Button(
        modifier = Modifier.padding(vertical = 8.dp),
        onClick = {
            if (checkSDKVersion() && !permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
            if (permissionState.status.isGranted) {
                val loaded = loadData(context)
                entries.clear()
                entries.addAll(loaded)
            }
        }
    ) {
        Text(buttonText)
    }

    LabelValueList(entries)
}

@Composable
private fun HeadLine(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 24.dp)
    )
}

@Composable
private fun SendBroadcast(context: Context, actionName: String, broadcastName: String) {
    Button(
        onClick = {
            val intent = Intent(actionName).apply {
                setPackage(context.packageName)
            }
            context.sendBroadcast(intent)
        },
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text("Send %s Broadcast".format(broadcastName))
    }
}


@Composable
fun LabelValueList(entries: List<DisplayEntry>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(top = 8.dp)
    ) {
        items(entries) { entry ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = entry.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = entry.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


private fun checkSDKVersion(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU


data class DisplayEntry(
    val label: String,
    val value: String
)