package com.june0122.wakplus.data.repository.impl

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.june0122.wakplus.R
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

    override suspend fun flowTwitchAccessTokens(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading access token preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[TWITCH_ACCESS_TOKEN] ?: ""
        }

    override suspend fun updateTheme(themeResourseId: Int) {
        dataStore.edit { preferences ->
            preferences[APP_THEME_NAME] = themeResourseId
        }
    }

    override suspend fun flowThemes(): Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading theme preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[APP_THEME_NAME] ?: R.style.Theme_Ine
        }

    suspend fun updateDarkModeId(darkModeId: Int) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_ID] = darkModeId
        }
    }

    suspend fun flowDarkModeIds(): Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading dark mode id preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[DARK_MODE_ID] ?: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }

    companion object {
        private val TWITCH_ACCESS_TOKEN = stringPreferencesKey("twitch_access_token")
        private val APP_THEME_NAME = intPreferencesKey("app_theme_name")
        private val DARK_MODE_ID = intPreferencesKey("dark_mode_id")
    }
}