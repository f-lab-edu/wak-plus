package com.june0122.wakplus.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.SnsPlatformEntity
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

    private val _favorites = MutableLiveData<List<Content>>()
    val favorites: LiveData<List<Content>> = _favorites

    private val _snsPlatforms = MutableLiveData<List<SnsPlatformEntity>>()
    val snsPlatforms: LiveData<List<SnsPlatformEntity>> = _snsPlatforms

    init {
        repository.flowAllSnsPlatforms()
            .onEach { snsPlatforms ->
                _snsPlatforms.value = snsPlatforms
            }.launchIn(viewModelScope)

        repository.flowAllFavorites()
            .onEach { favorites ->
                _favorites.value = (_favorites.value?.toMutableList() ?: mutableListOf()).apply {
                    clear()
                    addAll(favorites)
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

    override fun onFavoriteClick(content: Content) {
        deleteFavorite(content)
    }

    private fun deleteFavorite(content: Content) =
        viewModelScope.launch { repository.deleteFavorite(content) }
}