package com.june0122.wakplus.data.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveTwitchAccessToken(accessToken: String)

    suspend fun getTwitchAccessToken(): Flow<String>
}