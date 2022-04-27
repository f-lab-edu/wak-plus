package com.june0122.wakplus.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.api.TwitchAuthService
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.api.YoutubeService
import com.june0122.wakplus.data.entity.*
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.listeners.FavoriteClickListener
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
) : ViewModel(), StreamerClickListener, SnsClickListener, FavoriteClickListener {

    @Inject
    lateinit var twitchService: TwitchService
    @Inject
    lateinit var twitchAuthService: TwitchAuthService
    @Inject
    lateinit var youtubeService: YoutubeService

    lateinit var contentListAdapter: ContentListAdapter
    lateinit var streamerListAdapter: StreamerListAdapter
    lateinit var snsListAdapter: SnsListAdapter

    private var currentIdSet: IdSet? = null
    private var currentSns: SnsPlatformEntity = SnsPlatformEntity("전체", true)

    private val _snsPlatforms = MutableLiveData<List<SnsPlatformEntity>>()
    val snsPlatforms: LiveData<List<SnsPlatformEntity>> = _snsPlatforms

    private val _contents = MutableLiveData<List<ContentEntity>>()
    val contents: LiveData<List<ContentEntity>> = _contents

    private val _favorites = MutableLiveData<List<Favorite>>()
    val favorites: LiveData<List<Favorite>> = _favorites

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

        repository.favorites.onEach { favorites ->
            _favorites.value = (_favorites.value?.toMutableList() ?: mutableListOf()).apply {
                addAll(favorites.map { it.favorite })
                checkFavorites(favorites.map { it.favorite })
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

    override fun onFavoriteClick(content: ContentEntity) {
        setFavorite(content)
    }

    private fun insertFavorite(content: Favorite) =
        viewModelScope.launch {
            repository.insertFavorite(content)
        }

    private fun deleteFavorite(content: Favorite) =
        viewModelScope.launch {
            repository.deleteFavorite(content)
        }

    private fun setFavorite(content: ContentEntity) = viewModelScope.launch {
        when (content) {
            is TwitchVideoEntity -> {
                if (repository.compareInfo(content.contentId)) {
                    deleteFavorite(Favorite(content.contentId))
                } else {
                    insertFavorite(Favorite(content.contentId))
                }
            }
            is YoutubeVideoEntity -> {
                if (repository.compareInfo(content.contentId)) {
                    deleteFavorite(Favorite(content.contentId))
                } else {
                    insertFavorite(Favorite(content.contentId))
                }
            }
        }
    }

    fun checkFavorite(content: ContentEntity): Boolean {
        return when (content) {
            is TwitchVideoEntity -> {
                _favorites.value?.firstOrNull { it.contentFavortieId == content.contentId } != null
            }
            is YoutubeVideoEntity -> {
                _favorites.value?.firstOrNull { it.contentFavortieId == content.contentId } != null
            }
        }
    }

    private fun checkFavorites(favorites: List<Favorite>) {
        _contents.value = _contents.value?.map { content ->
            when (content) {
                is TwitchVideoEntity -> {
                    if (favorites.firstOrNull { it.contentFavortieId == content.contentId } != null) content.copy(
                        isFavorite = true
                    )
                    else content.copy(isFavorite = false)

                }
                is YoutubeVideoEntity -> {
                    if (favorites.firstOrNull { it.contentFavortieId == content.contentId } != null) content.copy(
                        isFavorite = true
                    )
                    else content.copy(isFavorite = false)
                }
            }
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

    private suspend fun getTwitchVideos(idSet: IdSet): List<ContentEntity> {
        val contentData = viewModelScope.async {
            // TODO: 저장된 Access Token을 사용하도록 변경
            val twitchVideos = twitchService.getChannelVideos(idSet.twitchId).data
            val twitchUserInfo = twitchService.getUserInfo(idSet.twitchId).data[0]
            val contents = twitchVideos.map { twitchVideoInfo ->
                TwitchVideoEntity(
                    contentId = twitchVideoInfo.id,
                    twitchVideoInfo = twitchVideoInfo,
                    twitchUserInfo = twitchUserInfo,
                    isFavorite = false
                )
            }
            contents
        }

        return contentData.await()
    }

    /** YOUTUBE */
    private suspend fun getYoutubeVideos(idSet: IdSet): List<ContentEntity> = withContext(Dispatchers.IO) {
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
                                    YoutubeVideoEntity(
                                        contentId = videoInfo.id,
                                        youtubeVideoInfo = videoInfo,
                                        profileUrl = getStreamerProfile(videoInfo),
                                        isFavorite = false
                                    )
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

    private fun List<ContentEntity>.sortByRecentUploads(): List<ContentEntity> {
        return this.sortedWith(
            compareByDescending { content ->
                when (content) {
                    is TwitchVideoEntity -> content.twitchVideoInfo.publishedAt
                    is YoutubeVideoEntity -> content.youtubeVideoInfo.snippet.publishedAt
                }
            }
        )
    }

    private suspend fun fetchSnsContents(idSet: IdSet): List<ContentEntity> {
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

    private fun updateContentsList(contents: List<ContentEntity>) = viewModelScope.launch {
        _contents.run {
            val checkedContent = contents.map { content ->
                when (content) {
                    is TwitchVideoEntity -> {
                        content.copy(isFavorite = repository.compareInfo(content.contentId))
                    }
                    is YoutubeVideoEntity -> {
                        content.copy(isFavorite = repository.compareInfo(content.contentId))
                    }
                }
            }
            postValue(mutableListOf())
            postValue(checkedContent.sortByRecentUploads())
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
                val contents = mutableListOf<ContentEntity>()
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