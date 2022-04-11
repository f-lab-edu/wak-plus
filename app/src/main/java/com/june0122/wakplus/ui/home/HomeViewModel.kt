package com.june0122.wakplus.ui.home

import androidx.lifecycle.*
import com.june0122.wakplus.data.api.TwitchAuthService
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.api.YoutubeService
import com.june0122.wakplus.data.entitiy.*
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: ContentRepository) : ViewModel() {
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

    private suspend fun getTwitchVideos(idSet: IdSet): List<ContentData> {
        val contentData = viewModelScope.async {
//            twitchService = TwitchService.create(getTwitchAccessToken())
            // TODO: 저장된 Access Token을 사용하도록 변경
            twitchService = TwitchService.create("3vxbemh6jsodq7919puu8e562om0yb")
            val twitchVideos = twitchService.getChannelVideos(idSet.twitchId).data
            val twitchUserInfo = twitchService.getUserInfo(idSet.twitchId).data[0]
            val contents = twitchVideos.map { twitchVideoInfo ->
                TwitchVideoEntity(twitchUserInfo, twitchVideoInfo)
            }
            contents
        }

        return contentData.await()
    }

    /** YOUTUBE */
    private suspend fun getYoutubeVideos(idSet: IdSet): List<ContentData> {
        val contentData = viewModelScope.async {
            youtubeService = YoutubeService.create()
            val youtubeChannelVideos =
                youtubeService.getChannelVideos(channelId = idSet.youtubeId, order = "date").items
            val youtubeUserInfo = youtubeService.getUserInfo(channelId = idSet.youtubeId).items[0]
            val contents = youtubeChannelVideos.map { youtubeChannelVideo ->
                YoutubeVideoEntity(
                    youtubeUserInfo,
                    youtubeService.getVideoInfo(id = youtubeChannelVideo.id.videoId).items[0]
                )
            }
            contents
        }

        return contentData.await()
    }

    fun collectStreamerContents(idSet: IdSet) {
        viewModelScope.launch {
            val contents = getTwitchVideos(idSet) + getYoutubeVideos(idSet)

            _contents.value = (_contents.value?.toMutableList() ?: mutableListOf()).apply {
                clear()
                addAll(contents)
            }
        }
    }

    fun collectAllStreamersContents() {
        viewModelScope.launch {
            val contents = mutableListOf<ContentData>()

            repository.isedolStreamers.map { streamers ->
                streamers.map { streamer ->
                    launch {
                        contents.addAll(getTwitchVideos(streamer.idSet) + getYoutubeVideos(streamer.idSet))
                    }
                }
            }.collect()

            _contents.value = (_contents.value?.toMutableList() ?: mutableListOf()).apply {
                clear()
                addAll(contents)
            }
        }
    }
}