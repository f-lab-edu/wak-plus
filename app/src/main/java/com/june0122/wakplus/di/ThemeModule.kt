package com.june0122.wakplus.di

import com.june0122.wakplus.data.repository.impl.PreferencesRepositoryImpl
import com.june0122.wakplus.utils.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(
        coroutineScope: CoroutineScope,
        preferencesRepository: PreferencesRepositoryImpl
    ): ThemeManager {
        return ThemeManager(coroutineScope, preferencesRepository)
    }
}