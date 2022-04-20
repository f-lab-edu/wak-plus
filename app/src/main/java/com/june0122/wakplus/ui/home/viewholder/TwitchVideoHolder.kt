package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.TwitchUserInfo
import com.june0122.wakplus.data.entitiy.TwitchVideoInfo
import com.june0122.wakplus.databinding.ItemTwitchVideoBinding

class TwitchVideoHolder(private val binding: ItemTwitchVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(userInfo: TwitchUserInfo, videoInfo: TwitchVideoInfo) = with(binding) {
        this.userInfo = userInfo
        this.videoInfo = videoInfo
        executePendingBindings()
    }

    companion object {
        const val RES_MAX = "1280x720"
        const val RES_STANDARD = "640x480"
        const val RES_HIGH = "480x360"
        const val RES_MEDIUM = "320x180"
        const val RES_DEFAULT = "120x90"
    }
}