package com.june0122.wakplus.ui.settings.theme

import androidx.lifecycle.ViewModel
import com.june0122.wakplus.data.entity.Theme
import com.june0122.wakplus.utils.ThemeManager
import com.june0122.wakplus.utils.listeners.ThemeClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel(), ThemeClickListener {
    override fun onThemeClick(theme: Theme) {
        themeManager.saveTheme(theme.name)
    }
}