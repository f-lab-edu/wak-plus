package com.june0122.wakplus.data.repository.impl

import android.util.Log
import com.june0122.wakplus.BuildConfig
import com.june0122.wakplus.data.api.TwitchAuthService
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.entity.*
import com.june0122.wakplus.data.repository.TwitchRepository
import com.june0122.wakplus.utils.SNS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TwitchRepositoryImpl @Inject constructor(
    private val twitchApi: TwitchService,
    private val twitchAuthApi: TwitchAuthService,
    private val preferencesRepository: PreferencesRepositoryImpl,
) : TwitchRepository {
    override suspend fun getTwitchUserInfo(userId: String): TwitchUserInfo =
        twitchApi.getUserInfo(userId).data[0]

    override suspend fun getTwitchVideos(idSet: IdSet): List<Content> {
        val twitchVideos = try {
            Log.e("TEST", "NO EXCEPTION")
            twitchApi.getChannelVideos(idSet.twitchId).data
        } catch (e: Exception) {
            Log.e("TEST", "EXCEPTION : ${e.printStackTrace()}")
            storeTwitchAccessToken()
            twitchApi.getChannelVideos(idSet.twitchId).data
        }

        val twitchUserInfo = twitchApi.getUserInfo(idSet.twitchId).data[0]
        val contents = twitchVideos.map { twitchVideoInfo ->
            getTwitchContent(twitchVideoInfo, twitchUserInfo)
        }

        return contents
    }

    override suspend fun createTwitchAccessToken(): String = withContext(Dispatchers.IO) {
        twitchAuthApi.getAccessToken(
            BuildConfig.TWITCH_CLIENT_ID,
            BuildConfig.TWITCH_CLIENT_SECRET,
            GRANT_TYPE_CLIENT_CREDENTIALS
        ).accessToken
    }

    override suspend fun storeTwitchAccessToken() {
        preferencesRepository.saveTwitchAccessToken(createTwitchAccessToken())
    }

    override fun getTwitchContent(videoInfo: TwitchVideoInfo, userInfo: TwitchUserInfo) =
        Content(
            contentId = videoInfo.id,
            contentType = SNS.TWITCH,
            contentInfo = ContentInfo(
                videoInfo.id,
                videoInfo.streamId,
                videoInfo.userId,
                videoInfo.userLogin,
                videoInfo.userName,
                videoInfo.title,
                videoInfo.description,
                videoInfo.createdAt,
                videoInfo.publishedAt,
                videoInfo.url,
                PREFIX_TWITCH_CHANNEL_URL + userInfo.login,
                videoInfo.thumbnailUrl,
                videoInfo.viewable,
                videoInfo.viewCount,
                videoInfo.language,
                videoInfo.type,
                videoInfo.duration,
                "mutedSegments",
                userInfo.display_name,
                userInfo.profile_image_url
            ),
            profileUrl = userInfo.profile_image_url,
            isFavorite = false
        )

    companion object {
        private const val GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials"
        private const val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"

        private const val PREFIX_TWITCH_CHANNEL_URL = "https://www.twitch.tv/"
    }
}