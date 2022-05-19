package com.june0122.wakplus.data.repository

import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.entity.StreamerEntity
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun flowAllStreamers(): Flow<List<StreamerEntity>>

    fun flowAllSnsPlatforms(): Flow<List<SnsPlatformEntity>>

    fun flowAllFavorites(): Flow<List<Content>>

    suspend fun compareInfo(contentId: String): Boolean

    suspend fun insertFavorite(content: Content)

    suspend fun deleteFavorite(content: Content)
}