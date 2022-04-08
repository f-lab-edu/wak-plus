package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entitiy.TwitchUserInfos
import com.june0122.wakplus.data.entitiy.TwitchVideos
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.IOException

interface TwitchService {
    @GET("videos")
    suspend fun getChannelVideos(@Query("user_id") userId: String): TwitchVideos

    @GET("users")
    suspend fun getUserInfo(@Query("id") userId: String): TwitchUserInfos

    companion object {
        private const val BASE_URL = "https://api.twitch.tv/helix/"

        fun create(accessToken: String): TwitchService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(TwitchAuthInterceptor(accessToken))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TwitchService::class.java)
        }
    }

    class TwitchAuthInterceptor(private val accessToken: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Client-Id", "ho8a2b48jp7kpylb5uac3fpbug3pam")
                .build()
            proceed(newRequest)
        }
    }
}