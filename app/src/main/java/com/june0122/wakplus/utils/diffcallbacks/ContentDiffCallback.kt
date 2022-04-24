package com.june0122.wakplus.utils.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.june0122.wakplus.data.entity.ContentEntity
import com.june0122.wakplus.data.entity.TwitchVideoEntity
import com.june0122.wakplus.data.entity.YoutubeVideoEntity

class ContentDiffCallback : DiffUtil.ItemCallback<ContentEntity>() {
    override fun areItemsTheSame(oldItem: ContentEntity, newItem: ContentEntity): Boolean {
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

    override fun areContentsTheSame(oldItem: ContentEntity, newItem: ContentEntity): Boolean {
        return oldItem == newItem
    }
}