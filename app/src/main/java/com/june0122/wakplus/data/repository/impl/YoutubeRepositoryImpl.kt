package com.june0122.wakplus.data.repository.impl

import com.june0122.wakplus.data.api.YoutubeService
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.ContentInfo
import com.june0122.wakplus.data.entity.IdSet
import com.june0122.wakplus.data.entity.YoutubeVideoInfo
import com.june0122.wakplus.data.repository.YoutubeRepository
import com.june0122.wakplus.utils.SNS
import javax.inject.Inject

class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeApi: YoutubeService,
) : YoutubeRepository {
    override suspend fun getYoutubeVideos(
        idSet: IdSet,
        profileUrl: String,
        maxResults: Int,
    ): List<Content> {
        return youtubeApi.run {
            getChannelVideos(
                channelId = idSet.youtubeId,
                order = ORDER_BY_DATE,
                maxResults = maxResults
            )
                .items
                .map { video ->
                    getVideoInfo(id = video.id.videoId)
                        .items[0]
                        .let { videoInfo ->
                            getYoutubeContent(videoInfo, profileUrl)
                        }
                }
        }
    }

    // 할당량 최적화를 위해 플레이리스트를 통한 채널 비디오 검색
    override suspend fun getYoutubeVideosByPlaylist(idSet: IdSet, profileUrl: String): List<Content> {
        return youtubeApi.run {
            getPlaylists(channelId = idSet.youtubeId)
                .items
                .flatMap { playlist ->
                    getPlaylistItems(id = playlist.id)
                        .items
                        .filter { playlistItem ->
                            playlistItem.status.privacyStatus != PRIVACY_PRIVATE
                        }.map { publicItem ->
                            getVideoInfo(id = publicItem.snippet.resourceId.videoId)
                                .items[0]
                                .let { videoInfo ->
                                    getYoutubeContent(videoInfo, profileUrl)
                                }
                        }
                }
        }
    }

    override fun getYoutubeContent(videoInfo: YoutubeVideoInfo, profileUrl: String) =
        Content(
            contentId = videoInfo.id,
            contentType = SNS.YOUTUBE,
            contentInfo = ContentInfo(
                videoInfo.id,
                "streamId",
                videoInfo.snippet.channelId,
                "userLogin",
                videoInfo.snippet.channelTitle,
                videoInfo.snippet.title,
                videoInfo.snippet.description,
                "createdAt",
                videoInfo.snippet.publishedAt,
                PREFIX_YOUTUBE_VIDEO_URL + videoInfo.id,
                PREFIX_YOUTUBE_CHANNEL_URL + videoInfo.snippet.channelId,
                videoInfo.snippet.thumbnails.high.url,
                "viewable",
                videoInfo.statistics.viewCount,
                videoInfo.snippet.defaultAudioLanguage,
                videoInfo.kind,
                videoInfo.contentDetails.duration,
                "mutedSegments",
                "displayName",
                "profileImageUrl"
            ),
            profileUrl = profileUrl,
            isFavorite = false
        )

    companion object {
        private const val PREFIX_YOUTUBE_VIDEO_URL = "https://youtu.be/"
        private const val PREFIX_YOUTUBE_CHANNEL_URL = "https://www.youtube.com/channel/"

        private const val ORDER_BY_DATE = "date"
        private const val ORDER_BY_RATING = "rating"
        private const val ORDER_BY_RELEVANCE = "relevance"
        private const val ORDER_BY_TITLE = "title"
        private const val ORDER_BY_VIDEO_COUNT = "videoCount"
        private const val ORDER_BY_VIEW_COUNT = "viewCount"

        private const val PRIVACY_PUBLIC = "public"
        private const val PRIVACY_PRIVATE = "private"
    }
}