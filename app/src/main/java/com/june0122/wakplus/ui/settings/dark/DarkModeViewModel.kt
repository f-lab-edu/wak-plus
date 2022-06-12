package com.june0122.wakplus.ui.settings.dark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.june0122.wakplus.data.repository.impl.PreferencesRepositoryImpl
import com.june0122.wakplus.utils.ThemeManager
import com.june0122.wakplus.utils.listeners.DarkModeClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DarkModeViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val preferencesRepositoryImpl: PreferencesRepositoryImpl
) : ViewModel(), DarkModeClickListener {

    private val _mode = MutableLiveData<Int>().also {
        viewModelScope.launch {
            it.value = preferencesRepositoryImpl.flowDarkModeIds().first()
        }
    }
    val mode: LiveData<Int> = _mode

    override fun onModeClick(mode: Int) {
        themeManager.saveActiveDarkMode(mode)
        _mode.value = mode
    }
}