package com.june0122.wakplus.data.repository

import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.IdSet
import com.june0122.wakplus.data.entity.YoutubeVideoInfo

interface YoutubeRepository {
    suspend fun getYoutubeVideos(idSet: IdSet, profileUrl: String): List<Content>

    suspend fun getYoutubeVideosByPlaylist(idSet: IdSet, profileUrl: String): List<Content>

    fun getYoutubeContent(videoInfo: YoutubeVideoInfo, profileUrl: String): Content
}