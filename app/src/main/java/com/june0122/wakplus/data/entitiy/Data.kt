package com.june0122.wakplus.data.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class ContentData

@Entity(tableName = "streamer_table")
data class StreamerEntity(
    @PrimaryKey val name: String,
    @ColumnInfo val profileUrl: String,
)

data class SnsPlatform(
    val serviceName: String,
)