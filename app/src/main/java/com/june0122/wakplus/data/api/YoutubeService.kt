package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entitiy.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {
    @GET("channels")
    suspend fun getUserInfo(
        @Query("key") key: String = "AIzaSyCfAkqsoGCK982DvNZhU-8hz8FIv4Zrj_8",
        @Query("part") part: String = "id, snippet",
        @Query("id") channelId: String,
    ): YoutubeUserInfos

    @GET("search")
    suspend fun getChannelVideos(
        @Query("key") key: String = "AIzaSyCfAkqsoGCK982DvNZhU-8hz8FIv4Zrj_8",
        @Query("part") part: String = "snippet",
        @Query("channelId") channelId: String,
        @Query("order") order: String,
        @Query("maxResults") maxResults: Int = 10,
    ): YoutubeChannelVideos

    @GET("videos")
    suspend fun getVideoInfo(
        @Query("key") key: String = "AIzaSyCfAkqsoGCK982DvNZhU-8hz8FIv4Zrj_8",
        @Query("part") part: String = "id, snippet, contentDetails, liveStreamingDetails, player, recordingDetails, statistics, status, topicDetails",
        @Query("id") id: String,
    ): YoutubeVideoInfos

    @GET("channelSections")
    suspend fun getChannelHomeInfo(
        @Query("key") key: String = "AIzaSyCfAkqsoGCK982DvNZhU-8hz8FIv4Zrj_8",
        @Query("part") part: String,
        @Query("channelId") channelId: String,
    ): YoutubeChannelHomeInfo

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        fun create(): YoutubeService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YoutubeService::class.java)
        }
    }
}