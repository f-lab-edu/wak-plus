package com.june0122.wakplus.di

import android.content.Context
import androidx.room.Room
import com.june0122.wakplus.data.room.ContentRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ContentRoomDatabase {
        return Room.databaseBuilder(
            context,
            ContentRoomDatabase::class.java,
            "content_database"
        ).build()
    }
}