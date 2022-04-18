package com.june0122.wakplus.data.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class ContentData

@Entity(tableName = "streamer_table")
data class StreamerEntity(
    @PrimaryKey val name: String,
    @ColumnInfo val profileUrl: String,
    @ColumnInfo val idSet: IdSet,
    val isSelected: Boolean,
)

data class IdSet(
    val twitchId: String,
    val youtubeId: String,
)

@Entity(tableName = "sns_table")
data class SnsPlatformEntity(
    @PrimaryKey val serviceName: String,
    val isSelected: Boolean,
)