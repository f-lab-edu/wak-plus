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

//    @Query("SELECT * FROM favorite_table " +
//            "INNER JOIN twitch_table ON twitch_table.contentId = favorite_table.contentFavortieId " +
//            "INNER JOIN youtube_table ON twitch_table.contentId = favorite_table.contentFavortieId "
//    )
//    fun getFavorites(): Flow<List<Favorite>>

    @Transaction
    @Query("SELECT * FROM favorite_table")
    fun getFavoritesAndContents(): Flow<List<FavoriteWithContents>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT COUNT(*) FROM favorite_table WHERE contentFavortieId LIKE :contentId")
    suspend fun compareInfo(contentId: String): Int
}