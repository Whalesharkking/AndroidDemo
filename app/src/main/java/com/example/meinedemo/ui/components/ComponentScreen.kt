package com.example.meinedemo.ui.components

import android.content.Intent
import android.content.IntentFilter

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.lifecycle.compose.LifecycleStartEffect
import com.example.meinedemo.components.BroadcastActions
import com.example.meinedemo.components.MyLocalBroadcastReceiver
import com.example.meinedemo.components.MyManifestBroadcastReceiver

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

    // UI-Teil: Textanzeige
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = "Broadcasts",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary
    )

    // Button zum Senden eines Manifest-Broadcasts
    Button(
        onClick = {
            val intent = Intent(BroadcastActions.MANIFEST_ACTION).apply {
                setClass(context, MyManifestBroadcastReceiver::class.java)
            }
            context.sendBroadcast(intent)
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Send Manifest Broadcast")
    }

    // Button zum Senden eines lokalen Broadcasts
    Button(
        onClick = {
            val intent = Intent(BroadcastActions.LOCAL_ACTION).apply {
                setClass(context, MyLocalBroadcastReceiver::class.java)
            }
            context.sendBroadcast(intent)
        },
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text("Send Local Broadcast")
    }
}
