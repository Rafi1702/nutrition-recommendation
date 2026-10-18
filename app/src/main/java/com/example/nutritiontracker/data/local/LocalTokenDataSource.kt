package com.example.nutritiontracker.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.errorprone.annotations.CanIgnoreReturnValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


data class UserToken(
    val accessToken: String?,
    val refreshToken: String?,
)

interface LocalTokenDataSource {
    fun setToken(newToken: String)

    fun setRefreshToken(newToken: String)
    fun getUserToken(): UserToken

    suspend fun clearToken()
}

class LocalTokenDataSourceImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    LocalTokenDataSource {
    private val TOKEN_KEY = stringPreferencesKey("EXAMPLE_TOKEN_KEY")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("EXAMPLE_REFRESH_TOKEN_KEY")


    override fun setToken(newToken: String) {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = newToken
            }
        }
    }


    override fun setRefreshToken(newToken: String) {
        runBlocking{
            dataStore.edit { preferences ->
                preferences[REFRESH_TOKEN_KEY] = newToken
            }
        }
    }

    override fun getUserToken(): UserToken {
        return runBlocking {
            val combinedToken =
                getPreferencesFlow(TOKEN_KEY).combine(getPreferencesFlow(REFRESH_TOKEN_KEY)) { accessToken, refreshToken ->
                    UserToken(accessToken, refreshToken)
                }

            combinedToken.first()
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences -> preferences.remove(TOKEN_KEY) }
        dataStore.edit { preferences -> preferences.remove(REFRESH_TOKEN_KEY) }
    }

    fun getPreferencesFlow(key: Preferences.Key<String>): Flow<String?> {
        return dataStore.data.map { preferences -> preferences[key] }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @CanIgnoreReturnValue
    @Singleton
    @Provides
    fun provideLocalTokenDataSource(dataStore: DataStore<Preferences>): LocalTokenDataSource {
        return LocalTokenDataSourceImpl(dataStore = dataStore)
    }
}