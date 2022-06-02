package com.june0122.wakplus.utils

import android.content.Context
import com.june0122.wakplus.R

object ThemeManager {
    operator fun set(context: Context, activeTheme: String) {
        var themeRecourseID: Int = R.style.Theme_WakPlus_Ine

        when (activeTheme) {
            "Ine" -> themeRecourseID = R.style.Theme_WakPlus_Ine
            "Jingburger" -> themeRecourseID = R.style.Theme_WakPlus_Jingburger
        }
        context.setTheme(themeRecourseID)
    }
}