package com.june0122.wakplus.utils

import com.june0122.wakplus.R
import com.june0122.wakplus.data.repository.impl.PreferencesRepositoryImpl
import com.june0122.wakplus.utils.ISEDOL.INE
import com.june0122.wakplus.utils.ISEDOL.JINGBURGER
import com.june0122.wakplus.utils.ISEDOL.VIICHAN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val preferencesRepository: PreferencesRepositoryImpl,
) {
    fun saveTheme(activeTheme: String) {
        val themeResourseId = when (activeTheme) {
            INE -> R.style.Theme_Ine
            VIICHAN -> R.style.Theme_Viichan
            JINGBURGER -> R.style.Theme_Jingburger
            else -> R.style.Theme_Ine
        }

        coroutineScope.launch {
            preferencesRepository.updateTheme(themeResourseId)
        }
    }
}