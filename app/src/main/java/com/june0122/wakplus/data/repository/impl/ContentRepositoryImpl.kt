package com.june0122.wakplus.data.repository.impl

import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.entity.StreamerEntity
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.data.room.ContentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val contentDao: ContentDao
) : ContentRepository {
    override fun flowAllStreamers(): Flow<List<StreamerEntity>> =
        contentDao.getAllStreamers()

    override fun flowAllSnsPlatforms(): Flow<List<SnsPlatformEntity>> =
        contentDao.getAllSnsPlatforms()

    override fun flowAllFavorites(): Flow<List<Content>> =
        contentDao.getAllFavorites()

    override suspend fun compareInfo(contentId: String): Boolean = withContext(Dispatchers.IO) {
        contentDao.compareInfo(contentId) > 0
    }

    override suspend fun insertFavorite(content: Content) = withContext(Dispatchers.IO) {
        contentDao.insertFavorite(content)
    }

    override suspend fun deleteFavorite(content: Content) = withContext(Dispatchers.IO) {
        contentDao.deleteFavorite(content)
    }
}