package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.YoutubeVideoEntity
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding

class YoutubeVideoHolder(private val binding: ItemYoutubeVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(content: YoutubeVideoEntity) = with(binding) {
        this.content = content
        executePendingBindings()
    }
}