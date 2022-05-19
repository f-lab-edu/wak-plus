package com.june0122.wakplus.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.repository.impl.ContentRepositoryImpl
import com.june0122.wakplus.ui.home.adapter.ContentListAdapter
import com.june0122.wakplus.ui.home.adapter.SnsListAdapter
import com.june0122.wakplus.utils.SNS
import com.june0122.wakplus.utils.listeners.FavoriteClickListener
import com.june0122.wakplus.utils.listeners.SnsClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val contentRepository: ContentRepositoryImpl
) : ViewModel(), SnsClickListener, FavoriteClickListener {

    lateinit var snsListAdapter: SnsListAdapter
    lateinit var contentListAdapter: ContentListAdapter

    private var currentSns: SnsPlatformEntity =
        SnsPlatformEntity(SNS.ALL, true)

    private val cachedFavorites = mutableListOf<Content>()

    private val _favorites = MutableLiveData<List<Content>>()
    val favorites: LiveData<List<Content>> = _favorites

    private val _snsPlatforms = MutableLiveData<List<SnsPlatformEntity>>()
    val snsPlatforms: LiveData<List<SnsPlatformEntity>> = _snsPlatforms

    init {
        contentRepository.flowAllSnsPlatforms()
            .onEach { snsPlatforms ->
                _snsPlatforms.value = snsPlatforms
            }.launchIn(viewModelScope)

        contentRepository.flowAllFavorites()
            .onEach { favorites ->
                cachedFavorites.run {
                    clear()
                    addAll(favorites)
                }
                updateContentsList()
            }.launchIn(viewModelScope)
    }

    override fun onSnsClick(position: Int) {
        val selectedSns = snsListAdapter[position]
        currentSns = selectedSns

        _snsPlatforms.value = _snsPlatforms.value?.map { sns ->
            sns.copy(isSelected = sns == selectedSns)
        }

        updateContentsList()
    }

    override fun onFavoriteClick(content: Content) {
        deleteFavorite(content)
    }

    private fun deleteFavorite(content: Content) =
        viewModelScope.launch {
            contentRepository.deleteFavorite(content)
        }

    private fun updateContentsList() {
        _favorites.value = when (currentSns.serviceId) {
            SNS.ALL -> {
                cachedFavorites.toList()
            }
            else -> {
                cachedFavorites.filter { content ->
                    content.contentType == currentSns.serviceId
                }
            }
        }
    }
}