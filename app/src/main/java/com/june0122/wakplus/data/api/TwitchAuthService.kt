package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entity.TwitchToken
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitchAuthService {
    @POST("token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("grant_type") grantType: String,
    ): TwitchToken
}