package com.example.nutritiontracker.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


interface LocalTokenDataSource {
    suspend fun setToken(newToken: String)
    suspend fun getToken(): Flow<String?>
}

class LocalTokenDataSourceImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    LocalTokenDataSource {
    private val TOKEN_KEY = stringPreferencesKey("EXAMPLE_TOKEN_KEY")
    override suspend fun setToken(newToken: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = newToken
        }
    }

    override suspend fun getToken(): Flow<String?>{
       return dataStore.data.map{ preferences ->
           preferences[TOKEN_KEY]
        }
    }
}