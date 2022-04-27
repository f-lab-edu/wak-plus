package com.june0122.wakplus.utils.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.june0122.wakplus.data.entity.ContentEntity
import com.june0122.wakplus.data.entity.TwitchVideoEntity
import com.june0122.wakplus.data.entity.YoutubeVideoEntity

class ContentDiffCallback : DiffUtil.ItemCallback<ContentEntity>() {
    override fun areItemsTheSame(oldItem: ContentEntity, newItem: ContentEntity): Boolean {
        return when {
            oldItem is TwitchVideoEntity && newItem is TwitchVideoEntity -> {
                oldItem.contentId == newItem.contentId
            }
            oldItem is YoutubeVideoEntity && newItem is YoutubeVideoEntity -> {
                oldItem.contentId == newItem.contentId
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