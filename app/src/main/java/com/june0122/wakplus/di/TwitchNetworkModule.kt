package com.june0122.wakplus.di

import com.june0122.wakplus.data.api.TwitchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    // TODO: Access token 변경 및 보안 처리
    private const val ACCESS_TOKEN = "ywwhqstujqw66iy7ch4qyhapukulls"
    private const val BASE_URL = "https://api.twitch.tv/helix/"
    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(TwitchAuthInterceptor(ACCESS_TOKEN))
        .build()

    @Singleton
    @Provides
    fun provideTwitchService(): TwitchService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwitchService::class.java)
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