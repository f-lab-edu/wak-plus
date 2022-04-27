package com.june0122.wakplus.data.room

import androidx.room.*
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.entity.StreamerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("SELECT * FROM streamer_table")
    fun getStreamers(): Flow<List<StreamerEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStreamer(streamer: StreamerEntity)

    @Query("SELECT * FROM sns_table")
    fun getSnsPlatforms(): Flow<List<SnsPlatformEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSnsPlatform(sns: SnsPlatformEntity)

    @Query("SELECT * FROM content_table")
    fun getContents(): Flow<List<Content>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(content: Content)

    @Delete
    suspend fun deleteFavorite(content: Content)

    @Query("SELECT COUNT(*) FROM content_table WHERE contentId LIKE :contentId")
    suspend fun compareInfo(contentId: String): Int
}