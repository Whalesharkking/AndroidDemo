package com.example.meinedemo.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyLocalBroadcastReceiver : BroadcastReceiver() {

    // Wird aufgerufen, wenn ein passender Broadcast empfangen wurde
    override fun onReceive(context: Context?, intent: Intent?) {
        // Prüfen, ob die Action der erwarteten entspricht
        if (intent?.action == BroadcastActions.LOCAL_ACTION) {
            Log.d("MyLocalBroadcastReceiver", "Broadcast received")
        } else {
            // Fallback, falls eine andere oder keine Action gesetzt ist
            Log.d("MyLocalBroadcastReceiver", "Unknown intent action: ${intent?.action}")
        }
    }
}