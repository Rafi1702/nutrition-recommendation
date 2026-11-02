package com.example.nutritiontracker.data.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.nutritiontracker.data.model.user.UserProfileLocal
import com.google.errorprone.annotations.CanIgnoreReturnValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton


private const val DATASTORE_FILE_NAME = "user_profile.json"

@Module
@InstallIn(SingletonComponent::class)

object LocalUserProfileStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<UserProfileLocal> {
        return DataStoreFactory.create(
            serializer = UserProfilePreferenceSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { err ->
                throw err
            }),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.dataStoreFile(DATASTORE_FILE_NAME) }
        )
    }

    @CanIgnoreReturnValue
    @Singleton
    @Provides
    fun provideLocalUserDataSource(dataStore: DataStore<UserProfileLocal>): UserProfileLocalDataSourceImpl {
        return UserProfileLocalDataSourceImpl(dataStore = dataStore)
    }

}

interface UserProfileLocalDataSource {
    suspend fun setTemporaryUserProfile(userProfileLocal: UserProfileLocal): Result<Unit>
    suspend fun getTemporaryUserProfile(): Flow<Result<UserProfileLocal>>
    suspend fun deleteUserProfile(): Result<Unit>
}

object UserProfilePreferenceSerializer : Serializer<UserProfileLocal> {
    override val defaultValue: UserProfileLocal
        get() = throw Exception("User Must not Null")


    override suspend fun readFrom(input: InputStream): UserProfileLocal {
        try {
            return Json.decodeFromString(
                deserializer = UserProfileLocal.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read UserProfileLocal", e)
        }
    }

    override suspend fun writeTo(t: UserProfileLocal, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserProfileLocal.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
}

class UserProfileLocalDataSourceImpl @Inject constructor(private val dataStore: DataStore<UserProfileLocal>) :
    UserProfileLocalDataSource {
    override suspend fun setTemporaryUserProfile(userProfileLocal: UserProfileLocal): Result<Unit> {
        return try {
            dataStore.updateData { userProfileLocal }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTemporaryUserProfile(): Flow<Result<UserProfileLocal>> {
        return dataStore.data.map { userProfile ->
            Result.success(userProfile)
        }.catch { err ->
            emit(Result.failure(err))
        }
    }

    override suspend fun deleteUserProfile(): Result<Unit> {
        return try {
            dataStore.updateData { UserProfilePreferenceSerializer.defaultValue }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
