package com.june0122.wakplus.di

import com.june0122.wakplus.data.api.TwitchAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TwitchAuthModule {
    private const val BASE_URL = "https://id.twitch.tv/oauth2/"

    @Singleton
    @Provides
    fun provideTwitchAuthService(retrofitBuilder: Retrofit.Builder): TwitchAuthService {
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .build()
            .create(TwitchAuthService::class.java)
    }
}