package com.example.meinedemo.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.meinedemo.data.database.DemoDatabase
import com.example.meinedemo.data.database.entities.UserEntity
import com.example.meinedemo.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepository(
    private val context: Context
) {
    private val dao = DemoDatabase.getDatabase(context).userDao()
    val user: Flow<User> = context.dataStore.data.map { preferences ->
        val userName = preferences[PreferencesKeys.USER_NAME] ?: ""
        val userAge = preferences[PreferencesKeys.USER_AGE] ?: -1
        val userAuthorized = preferences[PreferencesKeys.USER_AUTHORIZED] == true
        User(
            name = userName,
            age = userAge,
            authorized = userAuthorized
        )
    }

    suspend fun addUser(user: User) {
        dao.insert(
            UserEntity(
                name = user.name,
                age = user.age,
                authorized = user.authorized
            )
        )
    }

    fun getAllUsers(): Flow<List<User>> {
        return dao.getAll().map { users ->
            users.map { user ->
                User(
                    name = user.name,
                    age = user.age,
                    authorized = user.authorized
                )
            }
        }
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
