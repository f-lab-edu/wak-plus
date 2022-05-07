package com.june0122.wakplus.data.entity

import com.google.gson.annotations.SerializedName

data class HolodexVideosItem(
    @SerializedName("available_at") val availableAt: String,
    @SerializedName("certainty") val certainty: String,
    @SerializedName("channel") val channel: Channel,
    @SerializedName("credits") val credits: Credits,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("id") val id: String,
    @SerializedName("link") val link: String,
    @SerializedName("live_viewers") val liveViewers: Int,
    @SerializedName("placeholderType") val placeholderType: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("start_actual") val startActual: String,
    @SerializedName("start_scheduled") val startScheduled: String,
    @SerializedName("status") val status: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String,
    @SerializedName("topic_id") val topicId: String,
    @SerializedName("type") val type: String
)

data class Channel(
    @SerializedName("english_name") val englishName: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("org") val org: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("subscriber_count") val subscriberCount: String,
    @SerializedName("type") val type: String,
    @SerializedName("video_count") val videoCount: String,
    @SerializedName("view_count") val viewCount: String
)

data class Credits(
    val bot: Bot
)

data class Bot(
    val link: String,
    val name: String,
    val user: String
)