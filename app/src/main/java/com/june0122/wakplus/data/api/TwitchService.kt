package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entitiy.TwitchUserInfos
import com.june0122.wakplus.data.entitiy.TwitchVideos
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchService {
    @GET("videos")
    suspend fun getChannelVideos(@Query("user_id") userId: String): TwitchVideos

    @GET("users")
    suspend fun getUserInfo(@Query("id") userId: String): TwitchUserInfos
}