package com.june0122.wakplus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ContentsApplication : Application() {

    fun sample() {
        BuildConfig.API_KEY
    }
}