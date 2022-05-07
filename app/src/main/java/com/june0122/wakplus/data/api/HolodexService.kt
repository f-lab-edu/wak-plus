package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entity.HolodexVideosItem
import retrofit2.http.GET
import retrofit2.http.Query

interface HolodexService {
    @GET("videos")
    suspend fun getChannelVideos(
        @Query("channel_id") channelId: String,
        @Query("limit") limit: Int = 25,
        @Query("order") order: String = "desc",
        @Query("include") include: String = "description,live_info,channel_stats",
        @Query("status") status: String = "past",
    ): List<HolodexVideosItem>
}