package com.june0122.wakplus.di

import com.june0122.wakplus.data.api.YoutubeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeNetworkModule {
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    @Singleton
    @Provides
    fun provideYoutubeService(): YoutubeService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeService::class.java)
    }
}