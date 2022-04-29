package com.june0122.wakplus.data.api

import com.june0122.wakplus.BuildConfig
import com.june0122.wakplus.data.entity.*
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {
    @GET("channels")
    suspend fun getUserInfo(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "id, snippet",
        @Query("id") channelId: String,
    ): YoutubeUserInfoSet

    @GET("search")
    suspend fun getChannelVideos(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("channelId") channelId: String,
        @Query("order") order: String,
        @Query("maxResults") maxResults: Int = 10,
    ): YoutubeChannelVideos

    @GET("videos")
    suspend fun getVideoInfo(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "id, snippet, contentDetails, liveStreamingDetails, player, recordingDetails, statistics, status, topicDetails",
        @Query("id") id: String,
    ): YoutubeVideoInfos

    @GET("playlists")
    suspend fun getPlaylists(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("channelId") channelId: String,
    ): YoutubePlaylists

    @GET("playlistItems")
    suspend fun getPlaylistItems(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "id, snippet, contentDetails, status",
        @Query("playlistId") id: String,
        @Query("maxResults") maxResults: Int = 10,
    ): YoutubePlaylistItems

    @GET("channelSections")
    suspend fun getChannelHomeInfo(
        @Query("key") key: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String,
        @Query("channelId") channelId: String,
    ): YoutubeChannelHomeInfo
}