package com.june0122.wakplus.utils.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.june0122.wakplus.data.entitiy.ContentData
import com.june0122.wakplus.data.entitiy.TwitchVideoEntity
import com.june0122.wakplus.data.entitiy.YoutubeVideoEntity

class ContentDiffCallback : DiffUtil.ItemCallback<ContentData>() {
    override fun areItemsTheSame(oldItem: ContentData, newItem: ContentData): Boolean {
        return when {
            oldItem is TwitchVideoEntity && newItem is TwitchVideoEntity -> {
                oldItem.twitchVideoInfo.id == newItem.twitchVideoInfo.id
            }
            oldItem is YoutubeVideoEntity && newItem is YoutubeVideoEntity -> {
                oldItem.youtubeVideoInfo.id == newItem.youtubeVideoInfo.id
            }
            else -> {
                oldItem == newItem
            }
        }
    }

    override fun areContentsTheSame(oldItem: ContentData, newItem: ContentData): Boolean {
        return oldItem == newItem
    }
}