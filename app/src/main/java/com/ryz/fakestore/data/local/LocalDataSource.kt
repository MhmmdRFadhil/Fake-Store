package com.ryz.fakestore.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class LocalDataSource(private val dataStore: DataStore<Preferences>) {

    val authToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val username: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }

    suspend fun saveTokenAuth(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun getAuthToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.firstOrNull()
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val USERNAME_KEY = stringPreferencesKey("username")
        val USER_ID_KEY = intPreferencesKey("user_id")
    }
}