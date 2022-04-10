package com.june0122.wakplus.ui.home

import androidx.lifecycle.*
import com.june0122.wakplus.data.api.TwitchAuthService
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.api.YoutubeService
import com.june0122.wakplus.data.entitiy.*
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(repository: ContentRepository) : ViewModel() {
    lateinit var contentListAdapter: ContentListAdapter

    private lateinit var twitchService: TwitchService
    private lateinit var twitchUserInfo: TwitchUserInfo
    private val twitchAuthService: TwitchAuthService = TwitchAuthService.create()

    private lateinit var youtubeService: YoutubeService

    private val _contents = MutableLiveData<List<ContentData>>()
    val contents: LiveData<List<ContentData>> = _contents

    val isedolStreamers: LiveData<List<StreamerEntity>> = repository.isedolStreamers.asLiveData()

    /** TWITCH */
    private suspend fun createTwitchAccessToken(): String = withContext(Dispatchers.IO) {
        twitchAuthService.getAccessToken(
            "ho8a2b48jp7kpylb5uac3fpbug3pam",
            "9i7r5g3bizajy8c8yl8uxpevp1hoza",
            "client_credentials"
        ).accessToken
    }

    //    // SharedPreference 또는 DB에 Access Token 저장시키기
    fun storeTwitchAccessToken() {
//        viewModelScope.launch { twitchService = TwitchService.create(getTwitchAccessToken()) }
    }

//    fun getTwitchUserInfo(userId: String) {
//        viewModelScope.launch {
//            val userInfo = twitchService.getUserInfo(userId).data[0]
//            Log.d("TEST", "User Info: ${userInfo.display_name}")
//        }
//    }

    suspend fun getTwitchUserInfo(userId: String): TwitchUserInfo = withContext(Dispatchers.IO) {
        twitchService.getUserInfo(userId).data[0]
    }

    fun getTwitchVideos(userId: String) {
        viewModelScope.launch {
//            twitchService = TwitchService.create(getTwitchAccessToken())

            // TODO: 저장된 Access Token을 사용하도록 변경
            twitchService = TwitchService.create("3vxbemh6jsodq7919puu8e562om0yb")
            val twitchVideos = twitchService.getChannelVideos(userId).data
            val twitchUserInfo = twitchService.getUserInfo(userId).data[0]
            val contents = twitchVideos.map { twitchVideoInfo ->
                TwitchVideoEntity(twitchUserInfo, twitchVideoInfo)
            }

            _contents.value = (_contents.value?.toMutableList() ?: mutableListOf()).apply {
                addAll(contents)
            }
        }
    }

    /** YOUTUBE */
    fun getYoutubeVideos(userId: String) {
        viewModelScope.launch {
            youtubeService = YoutubeService.create()
            val youtubeChannelVideos = youtubeService.getChannelVideos(channelId = userId, order = "date").items
            val youtubeUserInfo = youtubeService.getUserInfo(channelId = userId).items[0]
            val contents = youtubeChannelVideos.map { youtubeChannelVideo ->
                YoutubeVideoEntity(youtubeUserInfo, youtubeService.getVideoInfo(id = youtubeChannelVideo.id.videoId).items[0])
            }

            _contents.value = (_contents.value?.toMutableList() ?: mutableListOf()).apply {
                addAll(contents)
            }
        }
    }

    companion object {
        const val TWITCH_ID_INE = "702754423"
        const val TWITCH_ID_GOSEGU = "614351180"

        const val YOUTUBE_ID_INE = "UCroM00J2ahCN6k-0-oAiDxg"
        const val YOUTUBE_ID_GOSEGU = "UCV9WL7sW6_KjanYkUUaIDfQ"
        const val YOUTUBE_ID_VIICHAN = "UCs6EwgxKLY9GG4QNUrP5hoQ"
        const val YOUTUBE_ID_JURURU = "UCTifMx1ONpElK5x6B4ng8eg"
        const val YOUTUBE_ID_JINGBURGER = "UCHE7GBQVtdh-c1m3tjFdevQ"
        const val YOUTUBE_ID_LILPA = "UC-oCJP9t47v7-DmsnmXV38Q"
    }
}