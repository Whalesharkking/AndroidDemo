package com.example.meinedemo.components

import android.content.Context
import android.provider.ContactsContract
import com.example.meinedemo.ui.components.DisplayEntry

object ContactLoader {
    internal fun readContacts(context: Context): List<DisplayEntry> {
        val contactList = mutableListOf<DisplayEntry>()
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )?.let { cursor ->
            val nameIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                contactList.add(DisplayEntry(name, number))
            }

            cursor.close()
        }
        return contactList
    }
}