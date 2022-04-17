package com.june0122.wakplus.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.R
import com.june0122.wakplus.ui.home.viewholder.TwitchVideoHolder.Companion.RES_STANDARD
import com.june0122.wakplus.utils.Language
import com.june0122.wakplus.utils.timeAgo
import com.june0122.wakplus.utils.withSuffix

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("circularImageUrl")
    fun setCircularImage(view: ImageView, imageUrl: String) {
        view.load(imageUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }
    }

    @JvmStatic
    @BindingAdapter("thumbnailUrl")
    fun setThumbnail(view: ImageView, imageUrl: String) {
        view.load(imageUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }
    }

    private fun String.parseTwitchThumbnailUrl(): String = this.replace("%{width}x%{height}", RES_STANDARD)

    @JvmStatic
    @BindingAdapter("twitchThumbnailUrl")
    fun setTwitchThumbnailUrl(view: ImageView, imageUrl: String) {
        setThumbnail(view, imageUrl.parseTwitchThumbnailUrl())
    }

    @JvmStatic
    @BindingAdapter("viewCount")
    fun setViewCount(view: TextView, viewCount: String) {
        view.text = view.context.getString(R.string.view_count, viewCount.withSuffix(Language.KR))
    }

    @JvmStatic
    @BindingAdapter("elapsedTime")
    fun setElapsedTime(view: TextView, elapsedTime: String) {
        view.text = elapsedTime.timeAgo(view.context)
    }
}