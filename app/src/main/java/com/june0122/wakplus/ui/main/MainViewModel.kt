package com.june0122.wakplus.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.network.NetworkChecker
import com.june0122.wakplus.data.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkChecker: NetworkChecker
) : ViewModel() {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState

    init {
        viewModelScope.launch {
            networkChecker.networkState.collectLatest {
                _networkState.value = it
            }
        }
    }
}
