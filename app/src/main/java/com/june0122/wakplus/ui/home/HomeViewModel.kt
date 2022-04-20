package com.june0122.wakplus.ui.home

import android.util.Log
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
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.listeners.SnsClickListener
import com.june0122.wakplus.utils.listeners.StreamerClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel(), StreamerClickListener, SnsClickListener {

    @Inject lateinit var twitchService: TwitchService
    @Inject lateinit var twitchAuthService: TwitchAuthService
    @Inject lateinit var youtubeService: YoutubeService

    lateinit var contentListAdapter: ContentListAdapter
    lateinit var streamerListAdapter: StreamerListAdapter
    lateinit var snsListAdapter: SnsListAdapter

    private var currentIdSet: IdSet? = null
    private var currentSns: SnsPlatformEntity = SnsPlatformEntity("전체", true)

    private val _snsPlatforms = MutableLiveData<List<SnsPlatformEntity>>()
    val snsPlatforms: LiveData<List<SnsPlatformEntity>> = _snsPlatforms

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

        repository.snsPlatforms.onEach { snsPlatforms ->
            _snsPlatforms.value = (_snsPlatforms.value?.toMutableList() ?: mutableListOf()).apply {
                addAll(snsPlatforms)
            }
        }.launchIn(viewModelScope)
    }

    private var contentsJob: Job? = null

    override fun onStreamerClick(position: Int) {
        contentsJob?.cancel("다른 스트리머의 콘텐츠 로딩으로 인한 취소", CancellationException())

        val selectedStreamer = streamerListAdapter[position]
        currentIdSet = selectedStreamer.idSet
        _streamers.value = _streamers.value?.map { streamer ->
            streamer.copy(
                isSelected = streamer == selectedStreamer && streamer.isSelected.not()
            )
        }

        if (selectedStreamer.isSelected) { // 이미 선택된 상태일 때
            currentIdSet = null
            collectAllStreamersContents()
        } else {
            currentIdSet = selectedStreamer.idSet
            collectStreamerContents(selectedStreamer.idSet)
        }
    }

    override fun onStreamerLongClick(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onSnsClick(position: Int) {
        contentsJob?.cancel("다른 SNS의 콘텐츠 로딩으로 인한 취소", CancellationException())
        val selectedSns = snsListAdapter[position]
        currentSns = selectedSns
        _snsPlatforms.value = _snsPlatforms.value?.map { sns ->
            sns.copy(isSelected = sns == selectedSns)
        }

        if (currentIdSet != null) {
            collectStreamerContents(currentIdSet ?: return)
        } else {
            collectAllStreamersContents()
        }
    }

    /** TWITCH */
    private suspend fun createTwitchAccessToken(): String = withContext(Dispatchers.IO) {
        twitchAuthService.getAccessToken(
            "ho8a2b48jp7kpylb5uac3fpbug3pam",
            "9i7r5g3bizajy8c8yl8uxpevp1hoza",
            "client_credentials"
        ).accessToken
    }

    // TODO: SharedPreference 또는 DB에 Access Token 저장시키기
    fun storeTwitchAccessToken() {
//        viewModelScope.launch { twitchService = TwitchService.create(getTwitchAccessToken()) }
    }

    suspend fun getTwitchUserInfo(userId: String): TwitchUserInfo = withContext(Dispatchers.IO) {
        twitchService.getUserInfo(userId).data[0]
    }

    private suspend fun getTwitchVideos(idSet: IdSet): List<ContentData> {
        val contentData = viewModelScope.async {
//            twitchService = TwitchService.create(getTwitchAccessToken())
            // TODO: 저장된 Access Token을 사용하도록 변경
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
        youtubeService.run {
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

    private suspend fun fetchSnsContents(idSet: IdSet): List<ContentData> {
        return when (currentSns.serviceName) {
            "전체" -> {
                /** Youtube API 할당량을 많이 소모하는 작업이기에 임시로 주석 처리 */
                // (getTwitchVideos(idSet) + getYoutubeVideos(idSet)).sortByRecentUploads()
                mutableListOf()
            }
            "트위치" -> {
                getTwitchVideos(idSet)
            }
            "유튜브" -> {
                getYoutubeVideos(idSet)
            }
            else -> {
                mutableListOf()
            }
        }
    }

    private fun updateContentsList(contents: List<ContentData>) = viewModelScope.launch {
        _contents.run {
            postValue(mutableListOf())
            postValue(contents.sortByRecentUploads())
        }
    }

    private fun collectStreamerContents(idSet: IdSet) {
        contentsJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val contents = fetchSnsContents(idSet)
                updateContentsList(contents)
            } catch (e: CancellationException) {
                Log.e("TEST", "${e.message}")
            } finally {
                Log.d("TEST", "Close contentsJob resources in finally")
            }
        }
    }

    fun collectAllStreamersContents() {
        contentsJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val contents = mutableListOf<ContentData>()
                repository.isedolStreamers.onEach { streamers ->
                    streamers.forEach { streamer ->
                        contents.addAll(fetchSnsContents(streamer.idSet))
                    }
                    updateContentsList(contents)
                }.collect()
            } catch (e: CancellationException) {
                Log.e("TEST", "${e.message}")
            } finally {
                Log.d("TEST", "Close contentsJob resources in finally")
            }
        }
    }
}