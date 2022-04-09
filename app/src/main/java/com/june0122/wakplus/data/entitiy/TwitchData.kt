package com.june0122.wakplus.data.entitiy

import com.google.gson.annotations.SerializedName

data class TwitchVideoEntity(
    val twitchUserInfo: TwitchUserInfo,
    val twitchVideoInfo: TwitchVideoInfo,
) : ContentData()

data class TwitchToken(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: String,
    @SerializedName("token_type") val tokenType: String,
)

data class TwitchVideos(
    @SerializedName("data") val data: List<TwitchVideoInfo>,
    @SerializedName("pagination") val pagination: Pagination
)

data class Pagination(@SerializedName("cursor") val cursor: String)

data class TwitchVideoInfo(
    @SerializedName("id") val id: String,
    @SerializedName("stream_id") val streamId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_login") val userLogin: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("viewable") val viewable: String,
    @SerializedName("view_count") val viewCount: String,
    @SerializedName("language") val language: String,
    @SerializedName("type") val type: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("muted_segments") val mutedSegments: String,
)

/** Get User Info */
data class TwitchUserInfos(
    @SerializedName("data") val data: List<TwitchUserInfo>
)

data class TwitchUserInfo(
    @SerializedName("broadcaster_type") val broadcaster_type: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("description") val description: String,
    @SerializedName("display_name") val display_name: String,
    @SerializedName("id") val id: String,
    @SerializedName("login") val login: String,
    @SerializedName("offline_image_url") val offline_image_url: String,
    @SerializedName("profile_image_url") val profile_image_url: String,
    @SerializedName("type") val type: String,
    @SerializedName("view_count") val view_count: Int
)

/** Search Channels */
data class TwitchChannelInfos(
    @SerializedName("data") val data: List<TwitchChannelInfo>,
    @SerializedName("pagination") val pagination: Pagination
)

data class TwitchChannelInfo(
    @SerializedName("broadcaster_language") val broadcasterLanguage: String,
    @SerializedName("broadcaster_login") val broadcasterLogin: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("game_id,") val gameId: String,
    @SerializedName("game_name") val gameName: String,
    @SerializedName("id") val id: String,
    @SerializedName("is_live") val isLive: Boolean,
    @SerializedName("started_at") val startedAt: String,
    @SerializedName("tag_ids") val tagIds: List<Any>,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("title") val title: String
)