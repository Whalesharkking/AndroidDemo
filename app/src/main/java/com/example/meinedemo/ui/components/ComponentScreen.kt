package com.example.meinedemo.ui.components

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.ContactsContract
import android.provider.Telephony
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.meinedemo.components.MyLocalBroadcastReceiver
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

    // Box, um alles auf dem Bildschirm zu zentrieren
    Box(
        modifier = Modifier
            .fillMaxSize(), // nimmt den ganzen verfügbaren Platz ein
        contentAlignment = Alignment.Center // zentriert den Inhalt
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Broadcasts",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    val intent = Intent(BroadcastActions.MANIFEST_ACTION).apply {
                        setPackage(context.packageName)
                    }
                    context.sendBroadcast(intent)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Send Manifest Broadcast")
            }

            Button(
                onClick = {
                    val intent = Intent(BroadcastActions.LOCAL_ACTION).apply {
                        setPackage(context.packageName)
                    }
                    context.sendBroadcast(intent)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Send Local Broadcast")
            }
            Text(
                text = "Content Provider",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            SmsWidget(context)
            ContactWidget(context)
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsWidget(context: Context) {

    val permissionState =
        rememberPermissionState(android.Manifest.permission.READ_SMS)
    val messages = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    Button(
        onClick = {
            if (permissionState.status.isGranted) {
                val loadedMessages = readSms(context)
                messages.clear()
                messages.addAll(loadedMessages)
            }
        },
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text("Read SMS")
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // <--- fixe Höhe
            .padding(top = 8.dp)
    ) {
        items(messages) { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactWidget(context: Context) {

    val permissionState =
        rememberPermissionState(android.Manifest.permission.READ_CONTACTS)
    val contacts = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    Button(
        onClick = {
            if (permissionState.status.isGranted) {
                val loadedContacts = readContacts(context)
                contacts.clear()
                contacts.addAll(loadedContacts)
            }
        },
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text("Read Contacts")
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // <--- fixe Höhe
            .padding(top = 8.dp)
    ) {
        items(contacts) { contact ->
            val parts = contact.split(":").map { it.trim() }
            val name = parts.getOrNull(0) ?: "Unbekannt"
            val number = parts.getOrNull(1) ?: "Keine Nummer"

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = number,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }


}

@Composable
fun LazyColumn(context: Context) {

}

private fun readSms(context: Context): List<String> {
    val smsList = mutableListOf<String>()
    context.contentResolver.query(
        Telephony.Sms.Inbox.CONTENT_URI,
        arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.BODY), // projection
        null,   // selection
        null,   // selection args
        null    // sort order
    )?.let { cursor ->
        val bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY)

        while (cursor.moveToNext()) {
            val body = cursor.getString(bodyIndex)
            smsList.add(body)
        }

        cursor.close()
    }
    return smsList
}

private fun readContacts(context: Context): List<String> {
    val contactList = mutableListOf<String>()
    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ), // projection
        null,   // selection
        null,   // selection args
        null    // sort order
    )?.let { cursor ->
        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (cursor.moveToNext()) {
            val name = cursor.getString(nameIndex)
            val number = cursor.getString(numberIndex)
            contactList.add("$name: $number")
        }

        cursor.close()
    }
    return contactList
}
