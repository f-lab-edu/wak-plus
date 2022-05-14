package com.june0122.wakplus.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.BuildConfig
import com.june0122.wakplus.data.api.TwitchAuthService
import com.june0122.wakplus.data.api.TwitchService
import com.june0122.wakplus.data.api.YoutubeService
import com.june0122.wakplus.data.entity.*
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.data.repository.PreferencesRepository
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.ui.home.adapter.StreamerListAdapter
import com.june0122.wakplus.utils.SNS
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
    private val repository: ContentRepository,
    private val preferencesRepository: PreferencesRepository
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
    private var currentSns: SnsPlatformEntity =
        SnsPlatformEntity(SNS.ALL, true)

    private val _snsPlatforms = MutableLiveData<List<SnsPlatformEntity>>()
    val snsPlatforms: LiveData<List<SnsPlatformEntity>> = _snsPlatforms

    private val _contents = MutableLiveData<List<Content>>()
    val contents: LiveData<List<Content>> = _contents

    private val _favorites = MutableLiveData<List<Content>>()
    val favorites: LiveData<List<Content>> = _favorites

    private val _streamers = MutableLiveData<List<StreamerEntity>>()
    val streamers: LiveData<List<StreamerEntity>> = _streamers

    init {
        repository.run {
            flowAllStreamers()
                .onEach { streamers -> _streamers.value = streamers }
                .launchIn(viewModelScope)

            flowAllSnsPlatforms()
                .onEach { snsPlatforms -> _snsPlatforms.value = snsPlatforms }
                .launchIn(viewModelScope)

            flowAllFavorites()
                .onEach { favorites ->
                    _favorites.value = favorites
                    checkFavorites(favorites)
                }
                .launchIn(viewModelScope)
        }
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

    override fun onFavoriteClick(content: Content) {
        setFavorite(content)
    }

    private fun insertFavorite(content: Content) =
        viewModelScope.launch {
            repository.insertFavorite(content)
        }

    private fun deleteFavorite(content: Content) =
        viewModelScope.launch {
            repository.deleteFavorite(content)
        }

    private fun setFavorite(content: Content) = viewModelScope.launch {
        if (repository.compareInfo(content.contentId)) {
            deleteFavorite(content)
        } else {
            insertFavorite(content.copy(isFavorite = true))
        }

    }

    private fun checkFavorites(favorites: List<Content>) {
        _contents.value = _contents.value?.map { content ->
            if (favorites.firstOrNull { it.contentId == content.contentId } != null) content.copy(
                isFavorite = true
            )
            else content.copy(isFavorite = false)
        }
    }

    /** TWITCH */
    private suspend fun createTwitchAccessToken(): String = withContext(Dispatchers.IO) {
        twitchAuthService.getAccessToken(
            BuildConfig.TWITCH_CLIENT_ID,
            BuildConfig.TWITCH_CLIENT_SECRET,
            "client_credentials"
        ).accessToken
    }

    private suspend fun storeTwitchAccessToken() {
        preferencesRepository.saveTwitchAccessToken(createTwitchAccessToken())
    }

    suspend fun getTwitchUserInfo(userId: String): TwitchUserInfo = withContext(Dispatchers.IO) {
        twitchService.getUserInfo(userId).data[0]
    }

    private suspend fun getTwitchVideos(idSet: IdSet): List<Content> {
        val contentData = viewModelScope.async(Dispatchers.IO) {

            val twitchVideos = try {
                Log.e("TEST", "NO EXCEPTION")
                twitchService.getChannelVideos(idSet.twitchId).data
            } catch (e: Exception) {
                Log.e("TEST", "EXCEPTION : ${e.printStackTrace()}")
                storeTwitchAccessToken()
                twitchService.getChannelVideos(idSet.twitchId).data
            }

            val twitchUserInfo = twitchService.getUserInfo(idSet.twitchId).data[0]
            val contents = twitchVideos.map { twitchVideoInfo ->
                Content(
                    contentId = twitchVideoInfo.id,
                    contentType = SNS.TWITCH,
                    contentInfo = ContentInfo(
                        twitchVideoInfo.id,
                        twitchVideoInfo.streamId,
                        twitchVideoInfo.userId,
                        twitchVideoInfo.userLogin,
                        twitchVideoInfo.userName,
                        twitchVideoInfo.title,
                        twitchVideoInfo.description,
                        twitchVideoInfo.createdAt,
                        twitchVideoInfo.publishedAt,
                        twitchVideoInfo.url,
                        "https://www.twitch.tv/" + twitchUserInfo.login,
                        twitchVideoInfo.thumbnailUrl,
                        twitchVideoInfo.viewable,
                        twitchVideoInfo.viewCount,
                        twitchVideoInfo.language,
                        twitchVideoInfo.type,
                        twitchVideoInfo.duration,
                        "twitchVideoInfo.mutedSegments",
                        twitchUserInfo.display_name,
                        twitchUserInfo.profile_image_url
                    ),
                    profileUrl = twitchUserInfo.profile_image_url,
                    isFavorite = false
                )
            }
            contents
        }

        return contentData.await()
    }

    /** YOUTUBE */
    private suspend fun getYoutubeVideos(idSet: IdSet): List<Content> = withContext(Dispatchers.IO) {
        youtubeService.run {
            getChannelVideos(channelId = idSet.youtubeId, order = "date")
                .items
                .map { video ->
                    getVideoInfo(id = video.id.videoId)
                        .items[0]
                        .let { videoInfo ->
                            Content(
                                contentId = videoInfo.id,
                                contentType = SNS.YOUTUBE,
                                contentInfo = ContentInfo(
                                    videoInfo.id,
                                    "videoInfo.streamId",
                                    videoInfo.snippet.channelId,
                                    "videoInfo.userLogin",
                                    videoInfo.snippet.channelTitle,
                                    videoInfo.snippet.title,
                                    videoInfo.snippet.description,
                                    "videoInfo.createdAt",
                                    videoInfo.snippet.publishedAt,
                                    "https://youtu.be/" + videoInfo.id,
                                    "https://www.youtube.com/channel/" + videoInfo.snippet.channelId,
                                    videoInfo.snippet.thumbnails.high.url,
                                    "videoInfo.viewable",
                                    videoInfo.statistics.viewCount,
                                    videoInfo.snippet.defaultAudioLanguage,
                                    videoInfo.kind,
                                    videoInfo.contentDetails.duration,
                                    "",
                                    "",
                                    ""
                                ),
                                profileUrl = getStreamerProfile(videoInfo),
                                isFavorite = false
                            )
                        }
                }
        }
    }

    // 할당량 최적화를 위해 플레이리스트를 통한 채널 비디오 검색
    private suspend fun getYoutubeVideosByPlaylist(idSet: IdSet): List<Content> = withContext(Dispatchers.IO) {
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
                                    Content(
                                        contentId = videoInfo.id,
                                        contentType = SNS.YOUTUBE,
                                        contentInfo = ContentInfo(
                                            videoInfo.id,
                                            "videoInfo.streamId",
                                            videoInfo.snippet.channelId,
                                            "videoInfo.userLogin",
                                            videoInfo.snippet.channelTitle,
                                            videoInfo.snippet.title,
                                            videoInfo.snippet.description,
                                            "videoInfo.createdAt",
                                            videoInfo.snippet.publishedAt,
                                            "https://youtu.be/" + videoInfo.id,
                                            "https://www.youtube.com/channel/" + videoInfo.snippet.channelId,
                                            videoInfo.snippet.thumbnails.high.url,
                                            "videoInfo.viewable",
                                            videoInfo.statistics.viewCount,
                                            videoInfo.snippet.defaultAudioLanguage,
                                            videoInfo.kind,
                                            videoInfo.contentDetails.duration,
                                            "",
                                            "",
                                            ""
                                        ),
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

    private fun List<Content>.sortByRecentUploads(): List<Content> {
        return this.sortedWith(
            compareByDescending { content ->
                content.contentInfo.publishedAt
            }
        )
    }

    private suspend fun fetchSnsContents(idSet: IdSet): List<Content> {
        return when (currentSns.serviceId) {
            SNS.ALL -> {
                /** Youtube API 할당량을 많이 소모하는 작업이기에 임시로 주석 처리 */
                // (getTwitchVideos(idSet) + getYoutubeVideos(idSet)).sortByRecentUploads()
                mutableListOf()
            }
            SNS.TWITCH -> {
                getTwitchVideos(idSet)
            }
            SNS.YOUTUBE -> {
                getYoutubeVideos(idSet)
            }
            else -> {
                mutableListOf()
            }
        }
    }

    private fun updateContentsList(contents: List<Content>) = viewModelScope.launch {
        _contents.run {
            val checkedContent = contents.map { content ->
                content.copy(isFavorite = repository.compareInfo(content.contentId))
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
                val contents = mutableListOf<Content>()
                repository.flowAllStreamers()
                    .onEach { streamers ->
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