package com.june0122.wakplus.data.entity

import androidx.room.*

@Entity(tableName = "content_table")
data class Content(
    @PrimaryKey val contentId: String,
    @ColumnInfo val contentType: String,
    @ColumnInfo val contentInfo: ContentInfo,
    @ColumnInfo val profileUrl: String,
    @ColumnInfo val isFavorite: Boolean,
)

data class ContentInfo(
    val id: String,
    val streamId: String,
    val userId: String,
    val userLogin: String,
    val userName: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val publishedAt: String,
    val url: String,
    val channelUrl: String,
    val thumbnailUrl: String,
    val viewable: String,
    val viewCount: String,
    val language: String,
    val type: String,
    val duration: String,
    val mutedSegments: String,
    val displayName: String,
    val profileImageUrl: String,
)

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