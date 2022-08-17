package com.june0122.wakplus.di

import com.june0122.wakplus.data.room.ContentDao
import com.june0122.wakplus.data.room.ContentRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentRepositoryModule {
    @Singleton
    @Provides
    fun provideContentDao(contentDatabase: ContentRoomDatabase): ContentDao {
        return contentDatabase.contentDao()
    }
}