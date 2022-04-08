package com.june0122.wakplus.data.entitiy

data class YoutubeVideo(
    val youtubeUserInfo: YoutubeUserInfo,
    val youtubeVideoInfo: YoutubeVideoInfo,
) : ContentData()

/** Get User Info */
data class YoutubeUserInfos(
    val etag: String,
    val items: List<YoutubeUserInfo>,
    val kind: String,
    val pageInfo: PageInfo
)

data class YoutubeUserInfo(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: UserInfoSnippet
)

data class UserInfoSnippet(
    val country: String,
    val description: String,
    val localized: Localized,
    val publishedAt: String,
    val thumbnails: Thumbnails,
    val title: String
)

data class Thumbnails(
    val default: Default,
    val high: High,
    val medium: Medium
)

/** Get Channel Videos */
data class YoutubeChannelVideos(
    val etag: String,
    val items: List<YoutubeChannelVideo>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo,
    val regionCode: String
)

data class YoutubeChannelVideo(
    val etag: String,
    val id: Id,
    val kind: String,
    val snippet: Snippet
)

data class PageInfo(
    val resultsPerPage: Int,
    val totalResults: Int
)

data class Id(
    val kind: String,
    val videoId: String
)

data class Snippet(
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val liveBroadcastContent: String,
    val publishTime: String,
    val publishedAt: String,
    val thumbnails: ChannelThumbnails,
    val title: String
)

data class ChannelThumbnails(
    val default: Default,
    val high: High,
    val medium: Medium
)

data class Default(
    val height: Int,
    val url: String,
    val width: Int
)

data class High(
    val height: Int,
    val url: String,
    val width: Int
)

data class Medium(
    val height: Int,
    val url: String,
    val width: Int
)

/** Get Video Info */
data class YoutubeVideoInfos(
    val etag: String,
    val items: List<YoutubeVideoInfo>,
    val kind: String,
    val pageInfo: PageInfo
)

data class YoutubeVideoInfo(
    val contentDetails: ContentDetails,
    val etag: String,
    val id: String,
    val kind: String,
    val player: Player,
    val recordingDetails: RecordingDetails,
    val snippet: VideoSnippet,
    val statistics: Statistics,
    val status: Status,
    val topicDetails: TopicDetails
): ContentData()

data class ContentDetails(
    val caption: String,
    val contentRating: ContentRating,
    val definition: String,
    val dimension: String,
    val duration: String,
    val licensedContent: Boolean,
    val projection: String
)

data class Player(
    val embedHtml: String
)

class RecordingDetails

data class VideoSnippet(
    val categoryId: String,
    val channelId: String,
    val channelTitle: String,
    val defaultAudioLanguage: String,
    val description: String,
    val liveBroadcastContent: String,
    val localized: Localized,
    val publishedAt: String,
    val tags: List<String>,
    val thumbnails: VideoThumbnails,
    val title: String
)

data class Statistics(
    val commentCount: String,
    val favoriteCount: String,
    val likeCount: String,
    val viewCount: String
)

data class Status(
    val embeddable: Boolean,
    val license: String,
    val madeForKids: Boolean,
    val privacyStatus: String,
    val publicStatsViewable: Boolean,
    val uploadStatus: String
)

data class TopicDetails(
    val topicCategories: List<String>
)

class ContentRating

data class Localized(
    val description: String,
    val title: String
)

data class VideoThumbnails(
    val default: Default,
    val high: High,
    val maxres: Maxres,
    val medium: Medium,
    val standard: Standard
)

data class Maxres(
    val height: Int,
    val url: String,
    val width: Int
)

data class Standard(
    val height: Int,
    val url: String,
    val width: Int
)

/** Get Channel Home Info */
data class YoutubeChannelHomeInfo(
    val etag: String,
    val items: List<Item>,
    val kind: String
)

data class Item(
    val contentDetails: ChannelHomeContentDetails,
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: ChannelHomeSnippet
)

data class ChannelHomeContentDetails(
    val channels: List<String>,
    val playlists: List<String>
)

data class ChannelHomeSnippet(
    val channelId: String,
    val position: Int,
    val title: String,
    val type: String
)