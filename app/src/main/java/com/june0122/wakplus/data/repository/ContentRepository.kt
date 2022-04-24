package com.june0122.wakplus.data.repository

import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.entity.StreamerEntity
import com.june0122.wakplus.data.entity.TwitchVideoEntity
import com.june0122.wakplus.data.entity.TwitchVideoInfo
import com.june0122.wakplus.data.room.ContentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentRepository @Inject constructor(private val contentDao: ContentDao) {
    val isedolStreamers: Flow<List<StreamerEntity>> = contentDao.getStreamers()
    val snsPlatforms: Flow<List<SnsPlatformEntity>> = contentDao.getSnsPlatforms()
    val twitchFavorites: Flow<List<TwitchVideoEntity>> = contentDao.getTwitchFavorites()

    suspend fun compareInfo(contentInfo: TwitchVideoInfo): Boolean = withContext(Dispatchers.IO) {
        contentDao.compareInfo(contentInfo) > 0
    }

    suspend fun insertFavorite(content: TwitchVideoEntity) = withContext(Dispatchers.IO) {
        contentDao.insertFavorite(content)
    }

    suspend fun deleteFavorite(content: TwitchVideoEntity) = withContext(Dispatchers.IO) {
        contentDao.deleteFavorite(content)
    }
}