package com.june0122.wakplus.data.entity

import androidx.room.*

sealed class ContentEntity

data class FavoriteWithContents(
    @Embedded val favorite: Favorite,
    @Relation(
        parentColumn = "contentId",
        entityColumn = "contentFavoriteId"
    )
    val contents: List<ContentEntity>
)

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey val contentFavortieId: String,
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