package com.june0122.wakplus.utils

import androidx.annotation.StringRes
import com.june0122.wakplus.R

enum class SNS(@StringRes val serviceName: Int) {
    ALL(R.string.sns_all),
    TWITCH(R.string.sns_twitch),
    YOUTUBE(R.string.sns_youtube),
    INSTAGRAM(R.string.sns_instagram),
    NAVER_CAFE(R.string.sns_naver_cafe),
    TWITTER(R.string.sns_twitter),
    SOUNDCLOUD(R.string.sns_soundcloud),
}