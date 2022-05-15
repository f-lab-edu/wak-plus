package com.june0122.wakplus.di

import com.june0122.wakplus.data.api.TwitchEventSubService
import com.june0122.wakplus.data.repository.PreferencesRepository
import com.june0122.wakplus.di.TwitchNetworkModule.TwitchAuthInterceptor
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
object TwitchEventSubModule {
    private const val BASE_URL = "https://api.twitch.tv/helix/eventsub/"

    @Singleton
    @Provides
    fun provideTwitchSubService(
        preferencesRepository: PreferencesRepository,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): TwitchEventSubService {
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(TwitchAuthInterceptor(preferencesRepository))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwitchEventSubService::class.java)
    }
}