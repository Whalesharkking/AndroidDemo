package com.example.meinedemo.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val context: Context
) {
    val user: Flow<User> = context.dataStore.data.map { preferences ->
        val userName = preferences[PreferencesKeys.USER_NAME] ?: ""
        val userAge = preferences[PreferencesKeys.USER_AGE] ?: -1
        val userAuthorized = preferences[PreferencesKeys.USER_AUTHORIZED] ?: false
        User(
            name = userName,
            age = userAge,
            authorized = userAuthorized
        )
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("user_name")] = name
        }
    }

    suspend fun setUserAge(age: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_AGE] = age
        }
    }

    suspend fun setUserAuthorized(authorized: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_AUTHORIZED] = authorized
        }
    }

    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        private val Context.dataStore by preferencesDataStore(
            name = USER_PREFERENCES_NAME
        )
    }

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_AGE = intPreferencesKey("user_age")
        val USER_AUTHORIZED = booleanPreferencesKey("user_authorized")
    }
}
