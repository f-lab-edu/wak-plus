package com.june0122.wakplus.utils

import android.content.Context
import com.june0122.wakplus.R

object ThemeManager {
    operator fun set(context: Context, activeTheme: String) {
        var themeRecourseID: Int = R.style.Theme_Ine

        when (activeTheme) {
            "Ine" -> themeRecourseID = R.style.Theme_Ine
            "Jingburger" -> themeRecourseID = R.style.Theme_Jingburger
            "Viichan" -> themeRecourseID = R.style.Theme_Viichan
        }
        context.setTheme(themeRecourseID)
    }
}