package com.june0122.wakplus.di

import android.content.Context
import com.june0122.wakplus.data.room.ContentRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContentDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context, scope: CoroutineScope): ContentRoomDatabase =
        ContentRoomDatabase.getDatabase(context, scope)
}