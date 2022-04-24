package com.june0122.wakplus.data.room

import androidx.room.*
import com.june0122.wakplus.data.entity.*
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

    @Query("SELECT * FROM twitch_table")
    fun getTwitchFavorites(): Flow<List<TwitchVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(content: TwitchVideoEntity)

    @Delete
    suspend fun deleteFavorite(content: TwitchVideoEntity)

    @Query("SELECT COUNT(*) FROM twitch_table WHERE twitchVideoInfo LIKE :contentInfo")
    suspend fun compareInfo(contentInfo: TwitchVideoInfo): Int
}