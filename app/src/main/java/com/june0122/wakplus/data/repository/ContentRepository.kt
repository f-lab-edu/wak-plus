package com.june0122.wakplus.data.repository

import com.june0122.wakplus.data.entity.*
import com.june0122.wakplus.data.room.ContentDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentRepository @Inject constructor(private val contentDao: ContentDao) {
    val isedolStreamers: Flow<List<StreamerEntity>> = contentDao.getStreamers()
    val snsPlatforms: Flow<List<SnsPlatformEntity>> = contentDao.getSnsPlatforms()
//    val favorites: Flow<List<Favorite>> = contentDao.getFavorites()
    val favorites: Flow<List<FavoriteWithContents>> = contentDao.getFavoritesAndContents()

    suspend fun compareInfo(contentId: String): Boolean = withContext(Dispatchers.IO) {
        contentDao.compareInfo(contentId) > 0
    }

    suspend fun insertFavorite(favorite: Favorite) = withContext(Dispatchers.IO) {
        contentDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Favorite) = withContext(Dispatchers.IO) {
        contentDao.deleteFavorite(favorite)
    }
}