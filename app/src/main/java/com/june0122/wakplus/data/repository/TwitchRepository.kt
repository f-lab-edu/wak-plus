package com.june0122.wakplus.data.repository

import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.IdSet
import com.june0122.wakplus.data.entity.TwitchUserInfo
import com.june0122.wakplus.data.entity.TwitchVideoInfo

interface TwitchRepository {
    suspend fun getTwitchUserInfo(userId: String): TwitchUserInfo

    suspend fun getTwitchVideos(idSet: IdSet, maxResults: Int): List<Content>

    suspend fun createTwitchAccessToken(): String

    suspend fun storeTwitchAccessToken()

    fun getTwitchContent(videoInfo: TwitchVideoInfo, userInfo: TwitchUserInfo): Content
}