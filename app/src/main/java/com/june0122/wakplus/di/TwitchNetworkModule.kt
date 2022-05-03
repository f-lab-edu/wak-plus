package com.june0122.wakplus.di

import android.util.Log
import com.june0122.wakplus.BuildConfig
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TwitchNetworkModule {
    private const val BASE_URL = "https://api.twitch.tv/helix/"
    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    private lateinit var twitchAccessToken: String

    @Singleton
    @Provides
    fun provideTwitchService(preferencesRepository: PreferencesRepository): TwitchService {

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(TwitchAuthInterceptor(preferencesRepository))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwitchService::class.java)
    }

    class TwitchAuthInterceptor(
        private val preferencesRepository: PreferencesRepository
    ) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {

            runBlocking {
                twitchAccessToken = preferencesRepository.getTwitchAccessToken().first()
            }

            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer $twitchAccessToken")
                .addHeader("Client-Id", BuildConfig.TWITCH_CLIENT_ID)
                .build()

            proceed(newRequest)
        }
    }
}