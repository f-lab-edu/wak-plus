package com.june0122.wakplus.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.entity.*
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.utils.listeners.FavoriteClickListener
import com.june0122.wakplus.utils.listeners.SnsClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel(), SnsClickListener, FavoriteClickListener {

    lateinit var snsListAdapter: SnsListAdapter
    lateinit var contentListAdapter: ContentListAdapter

    private var currentSns: SnsPlatformEntity = SnsPlatformEntity("전체", true)

    private val _favorites = MutableLiveData<List<ContentEntity>>()
    val favorites: LiveData<List<ContentEntity>> = _favorites

    private val _snsPlatforms = MutableLiveData<List<SnsPlatformEntity>>()
    val snsPlatforms: LiveData<List<SnsPlatformEntity>> = _snsPlatforms

    init {
        repository.snsPlatforms.onEach { snsPlatforms ->
            _snsPlatforms.value = (_snsPlatforms.value?.toMutableList() ?: mutableListOf()).apply {
                addAll(snsPlatforms)
            }
        }.launchIn(viewModelScope)

        repository.favorites.onEach { favorites ->
            _favorites.value = (_favorites.value?.toMutableList() ?: mutableListOf()).apply {
                clear()
                addAll(favorites.flatMap { it.contents })
            }
        }.launchIn(viewModelScope)
    }

    override fun onSnsClick(position: Int) {
        val selectedSns = snsListAdapter[position]
        currentSns = selectedSns

        _snsPlatforms.value = _snsPlatforms.value?.map { sns ->
            sns.copy(isSelected = sns == selectedSns)
        }
    }

    override fun onFavoriteClick(content: ContentEntity) {
        when (content) {
            is TwitchVideoEntity -> deleteFavorite(Favorite(content.contentId))
            is YoutubeVideoEntity -> deleteFavorite(Favorite(content.contentId))
        }
    }

    private fun deleteFavorite(content: Favorite) =
        viewModelScope.launch { repository.deleteFavorite(content) }
}