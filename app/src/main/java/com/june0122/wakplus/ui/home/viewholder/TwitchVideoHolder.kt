package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entitiy.TwitchUserInfo
import com.june0122.wakplus.data.entitiy.TwitchVideoInfo
import com.june0122.wakplus.databinding.ItemTwitchVideoBinding
import com.june0122.wakplus.utils.Language
import com.june0122.wakplus.utils.timeAgo
import com.june0122.wakplus.utils.withSuffix

class TwitchVideoHolder(private val binding: ItemTwitchVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(userInfo: TwitchUserInfo, videoInfo: TwitchVideoInfo) = with(binding) {
        val thumbnailUrl = parseThumbnailUrl(videoInfo.thumbnailUrl)

        imgThumbnail.load(thumbnailUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
        }

        imgChannelProfile.load(userInfo.profile_image_url) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }

        tvDuration.text = videoInfo.duration
        tvVideoTitle.text = videoInfo.title
        tvChannelName.text = userInfo.display_name
        tvViewCount.text =
            itemView.context.getString(R.string.view_count, videoInfo.viewCount.withSuffix(Language.KR))
        tvElapsedTime.text = videoInfo.publishedAt.timeAgo(binding.root.context)
    }

    private fun parseThumbnailUrl(url: String): String = url.replace("%{width}x%{height}", RES_STANDARD)

    companion object {
        const val RES_MAX = "1280x720"
        const val RES_STANDARD = "640x480"
        const val RES_HIGH = "480x360"
        const val RES_MEDIUM = "320x180"
        const val RES_DEFAULT = "120x90"
    }
}