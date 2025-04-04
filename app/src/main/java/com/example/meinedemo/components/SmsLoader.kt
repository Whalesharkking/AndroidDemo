package com.example.meinedemo.components

import android.content.Context
import android.provider.Telephony
import com.example.meinedemo.ui.components.DisplayEntry

object SmsLoader {
    // Nur innerhalb des Moduls sichtbar
    internal fun readSms(context: Context): List<DisplayEntry> {
        val smsList = mutableListOf<DisplayEntry>()
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(
                Telephony.Sms.Inbox.BODY,
                Telephony.Sms.Inbox.ADDRESS
            ),
            null,
            null,
            null
        )?.let { cursor -> //Null-Safety falls kein SMS vorhanden
            val bodyIndex = cursor.getColumnIndex(Telephony.Sms.BODY)
            val addressIndex = cursor.getColumnIndex(Telephony.Sms.ADDRESS)

            while (cursor.moveToNext()) {
                val body = cursor.getString(bodyIndex)
                val address = cursor.getString(addressIndex)
                smsList.add(DisplayEntry(address, body))
            }

            cursor.close()
        }
        return smsList
    }
}