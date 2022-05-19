package com.june0122.wakplus.data.repository.impl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.june0122.wakplus.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {
    private val TAG: String = "UserPreferencesRepo"

    override suspend fun saveTwitchAccessToken(accessToken: String) {
        dataStore.edit { prefercences ->
            prefercences[TWITCH_ACCESS_TOKEN] = accessToken
        }
    }

    override suspend fun getTwitchAccessToken(): Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[TWITCH_ACCESS_TOKEN] ?: ""
        }

    companion object {
        private val TWITCH_ACCESS_TOKEN = stringPreferencesKey("twitch_access_token")
    }
}