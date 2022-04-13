package com.june0122.wakplus.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.june0122.wakplus.data.entitiy.StreamerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("SELECT * FROM streamer_table")
    fun getStreamers(): Flow<List<StreamerEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStreamer(streamer: StreamerEntity)
}