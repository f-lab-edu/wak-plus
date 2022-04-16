package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.YoutubeVideoInfo
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding

class YoutubeVideoHolder(private val binding: ItemYoutubeVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(profileUrl: String, videoInfo: YoutubeVideoInfo) = with(binding) {
        this.profileUrl = profileUrl
        this.videoInfo = videoInfo
        executePendingBindings()
    }
}