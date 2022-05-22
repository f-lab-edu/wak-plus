package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entity.TwitchUserInfoSet
import com.june0122.wakplus.data.entity.TwitchVideos
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchService {
    @GET("videos")
    suspend fun getChannelVideos(
        @Query("user_id") userId: String,
        @Query("first") maxResults: Int = 20,
    ): TwitchVideos

    @GET("users")
    suspend fun getUserInfo(@Query("id") userId: String): TwitchUserInfoSet
}