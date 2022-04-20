package com.june0122.wakplus.data.repository

import com.june0122.wakplus.data.entitiy.SnsPlatformEntity
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.data.room.ContentDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContentRepository @Inject constructor(contentDao: ContentDao) {
    val isedolStreamers: Flow<List<StreamerEntity>> = contentDao.getStreamers()
    val snsPlatforms: Flow<List<SnsPlatformEntity>> = contentDao.getSnsPlatforms()
}