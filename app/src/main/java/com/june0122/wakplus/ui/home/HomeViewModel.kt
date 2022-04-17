package com.june0122.wakplus.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.api.TwitchAuthService
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.api.YoutubeService
import com.june0122.wakplus.data.entitiy.*
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.listeners.StreamerClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repository: ContentRepository) : ViewModel(), StreamerClickListener {

    lateinit var contentListAdapter: ContentListAdapter
    lateinit var streamerListAdapter: StreamerListAdapter

    private lateinit var twitchService: TwitchService
    private val twitchAuthService: TwitchAuthService = TwitchAuthService.create()

    private lateinit var youtubeService: YoutubeService

    private val _contents = MutableLiveData<List<ContentData>>()
    val contents: LiveData<List<ContentData>> = _contents

    private val _streamers = MutableLiveData<List<StreamerEntity>>()
    val streamers: LiveData<List<StreamerEntity>> = _streamers

    init {
        repository.isedolStreamers.onEach { streamers ->
            _streamers.value = (_streamers.value?.toMutableList() ?: mutableListOf()).apply {
                addAll(streamers)
            }
        }.launchIn(viewModelScope)
    }

    override fun onStreamerClick(position: Int) {
        val selectedStreamer = streamerListAdapter[position]
        _streamers.value = _streamers.value?.map { streamer ->
            streamer.copy(
                isSelected = streamer == selectedStreamer && streamer.isSelected.not()
            )
        }

        if (selectedStreamer.isSelected) {
            collectAllStreamersContents()
        } else {
            collectStreamerContents(streamerListAdapter[position].idSet)
        }
    }

    override fun onStreamerLongClick(position: Int) {
        TODO("Not yet implemented")
    }

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
            twitchService = TwitchService.create("ywwhqstujqw66iy7ch4qyhapukulls")
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
    private suspend fun getYoutubeVideos(idSet: IdSet): List<ContentData> = withContext(Dispatchers.IO) {
        YoutubeService.create().run {
            getPlaylists(channelId = idSet.youtubeId)
                .items
                .flatMap { playlist ->
                    getPlaylistItems(id = playlist.id)
                        .items
                        .filter { playlistItem ->
                            playlistItem.status.privacyStatus != "private"
                        }.map { publicItem ->
                            getVideoInfo(id = publicItem.snippet.resourceId.videoId)
                                .items[0]
                                .let { videoInfo ->
                                    YoutubeVideoEntity(getStreamerProfile(videoInfo), videoInfo)
                                }
                        }
                }
        }
    }

    private fun getStreamerProfile(videoInfo: YoutubeVideoInfo): String {
        var profileUrl = ""

        _streamers.value?.map { streamer ->
            if (videoInfo.snippet.channelId == streamer.idSet.youtubeId) {
                profileUrl = streamer.profileUrl
            }
        }

        return profileUrl
    }

    private fun List<ContentData>.sortByRecentUploads(): List<ContentData> {
        return this.sortedWith(
            compareByDescending { content ->
                when (content) {
                    is TwitchVideoEntity -> content.twitchVideoInfo.publishedAt
                    is YoutubeVideoEntity -> content.youtubeVideoInfo.snippet.publishedAt
                    else -> 0
                }
            }
        )
    }

    private fun collectStreamerContents(idSet: IdSet) {
        viewModelScope.launch {
            val contents = (getTwitchVideos(idSet) + getYoutubeVideos(idSet)).sortByRecentUploads()

            _contents.value = (_contents.value?.toMutableList() ?: mutableListOf()).apply {
                clear()
                addAll(contents)
            }
        }
    }

    fun collectAllStreamersContents() {
        viewModelScope.launch {
            val contents = mutableListOf<ContentData>()

            /** Youtube API 할당량을 많이 소모하는 작업이기에 임시로 주석 처리 */
//            repository.isedolStreamers.map { streamers ->
//                streamers.map { streamer ->
//                    launch {
//                        contents.addAll(getTwitchVideos(streamer.idSet) + getYoutubeVideos(streamer.idSet))
//                    }
//                }
//            }.collect()

            _contents.value = (_contents.value?.toMutableList() ?: mutableListOf()).apply {
                clear()
                addAll(contents)
            }
        }
    }
}