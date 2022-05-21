package com.june0122.wakplus.di

import com.june0122.wakplus.data.repository.impl.ContentRepositoryImpl
import com.june0122.wakplus.data.repository.impl.TwitchRepositoryImpl
import com.june0122.wakplus.data.repository.impl.YoutubeRepositoryImpl
import com.june0122.wakplus.ui.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Singleton
    @Provides
    fun provideHomeViewModel(
        contentRepository: ContentRepositoryImpl,
        twitchRepository: TwitchRepositoryImpl,
        youtubeRepository: YoutubeRepositoryImpl,
    ): HomeViewModel {
        return HomeViewModel(contentRepository, twitchRepository, youtubeRepository)
    }
}