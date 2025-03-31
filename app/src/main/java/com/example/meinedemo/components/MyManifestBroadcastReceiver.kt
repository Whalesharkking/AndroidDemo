package com.example.meinedemo.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

// Dedizierter BroadcastReceiver, reagiert auf bestimmte Aktionen
class MyManifestBroadcastReceiver : BroadcastReceiver() {

    // Wird aufgerufen, wenn ein passender Broadcast empfangen wurde
    override fun onReceive(context: Context?, intent: Intent?) {
        // Prüfen, ob die Action der erwarteten entspricht
        if (intent?.action == BroadcastActions.MANIFEST_ACTION) {
            Log.d("MyManifestBroadcastReceiver", "Broadcast received")
        } else {
            // Fallback, falls eine andere oder keine Action gesetzt ist
            Log.d("MyManifestBroadcastReceiver", "Unknown intent action: ${intent?.action}")
        }
    }
}
