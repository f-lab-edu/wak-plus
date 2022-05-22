package com.june0122.wakplus.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.IdSet
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.entity.StreamerEntity
import com.june0122.wakplus.data.repository.impl.ContentRepositoryImpl
import com.june0122.wakplus.data.repository.impl.TwitchRepositoryImpl
import com.june0122.wakplus.data.repository.impl.YoutubeRepositoryImpl
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
    private val contentRepository: ContentRepositoryImpl,
    private val twitchRepository: TwitchRepositoryImpl,
    private val youtubeRepository: YoutubeRepositoryImpl,
) : ViewModel(), StreamerClickListener, SnsClickListener, FavoriteClickListener {

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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        contentRepository.run {
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
            contentRepository.insertFavorite(content)
        }

    private fun deleteFavorite(content: Content) =
        viewModelScope.launch {
            contentRepository.deleteFavorite(content)
        }

    private fun setFavorite(content: Content) = viewModelScope.launch {
        if (contentRepository.compareInfo(content.contentId)) {
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

    private fun getStreamerYoutubeProfile(idSet: IdSet): String {
        var profileUrl = ""

        _streamers.value?.map { streamer ->
            if (idSet.youtubeId == streamer.idSet.youtubeId) {
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

    private suspend fun fetchSnsContents(idSet: IdSet, maxResults: Int): List<Content> =
        withContext(Dispatchers.IO) {
            when (currentSns.serviceId) {
                SNS.ALL -> {
                    /** Youtube API 할당량을 많이 소모하는 작업이기에 임시로 주석 처리 */
                    // (getTwitchVideos(idSet) + getYoutubeVideos(idSet)).sortByRecentUploads()
                    mutableListOf()
                }
                SNS.TWITCH -> {
                    twitchRepository.getTwitchVideos(idSet, maxResults)
                }
                SNS.YOUTUBE -> {
                    youtubeRepository.getYoutubeVideos(
                        idSet = idSet,
                        profileUrl = getStreamerYoutubeProfile(idSet),
                        maxResults = maxResults
                    )
                }
                else -> {
                    mutableListOf()
                }
            }
        }

    private fun updateContentsList(contents: List<Content>) = viewModelScope.launch {
        _contents.run {
            val checkedContent = contents.map { content ->
                content.copy(isFavorite = contentRepository.compareInfo(content.contentId))
            }
            postValue(mutableListOf())
            postValue(checkedContent.sortByRecentUploads())
        }
        _isLoading.postValue(false)
    }

    private fun collectStreamerContents(idSet: IdSet) {
        contentsJob = viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val contents = fetchSnsContents(idSet, MAX_RESULTS)
                updateContentsList(contents)
            } catch (e: CancellationException) {
                _isLoading.postValue(false)
                Log.e("TEST", "${e.message}")
            } finally {
                Log.d("TEST", "Close contentsJob resources in finally")
            }
        }
    }

    fun collectAllStreamersContents() {
        contentsJob = viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val contents = mutableListOf<Content>()
                contentRepository.flowAllStreamers()
                    .onEach { streamers ->
                        streamers.forEach { streamer ->
                            contents.addAll(
                                fetchSnsContents(streamer.idSet, MAX_RESULTS_ALL)
                            )
                        }
                        updateContentsList(contents)
                    }.collect()
            } catch (e: CancellationException) {
                _isLoading.postValue(false)
                Log.e("TEST", "${e.message}")
            } finally {
                Log.d("TEST", "Close contentsJob resources in finally")
            }
        }
    }

    companion object {
        private const val MAX_RESULTS = 10
        private const val MAX_RESULTS_ALL = 3
    }
}