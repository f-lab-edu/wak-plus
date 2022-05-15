package com.june0122.wakplus.di

import com.june0122.wakplus.data.api.YoutubeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeNetworkModule {
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    @Singleton
    @Provides
    fun provideYoutubeService(retrofitBuilder: Retrofit.Builder): YoutubeService {
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .build()
            .create(YoutubeService::class.java)
    }
}