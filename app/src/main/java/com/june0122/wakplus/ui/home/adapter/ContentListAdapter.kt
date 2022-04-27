package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.databinding.ItemTwitchVideoBinding
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding
import com.june0122.wakplus.ui.home.viewholder.TwitchVideoHolder
import com.june0122.wakplus.ui.home.viewholder.YoutubeVideoHolder
import com.june0122.wakplus.utils.diffcallbacks.ContentDiffCallback
import com.june0122.wakplus.utils.listeners.FavoriteClickListener

class ContentListAdapter(
    private val favoriteListener: FavoriteClickListener,
) : ListAdapter<Content, RecyclerView.ViewHolder>(ContentDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].contentType) {
            "twitch" -> VIEW_TYPE_TWITCH_VIDEO
            "youtube" -> VIEW_TYPE_YOUTUBE_VIDEO
            else -> VIEW_TYPE_TWITCH_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TWITCH_VIDEO -> {
                val binding =
                    ItemTwitchVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TwitchVideoHolder(binding, favoriteListener)
            }
            VIEW_TYPE_YOUTUBE_VIDEO -> {
                val binding =
                    ItemYoutubeVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YoutubeVideoHolder(binding, favoriteListener)
            }
            else -> {
                val tempBinding =
                    ItemTwitchVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TwitchVideoHolder(tempBinding, favoriteListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val content = currentList[holder.absoluteAdapterPosition]
        when (content.contentType) {
            "twitch" -> if (holder is TwitchVideoHolder) {
                holder.bind(content)
            }
            "youtube" -> if (holder is YoutubeVideoHolder) {
                holder.bind(content)
            }
        }
    }

    companion object {
        const val VIEW_TYPE_TWITCH_VIDEO = 0
        const val VIEW_TYPE_YOUTUBE_VIDEO = 1
        const val VIEW_TYPE_YOUTUBE_COMMUNITY = 2
        const val VIEW_TYPE_INSTAGRAM_ARTICLE = 3
        const val VIEW_TYPE_TWITTER_ARTICLE = 4
        const val VIEW_TYPE_NAVER_CAFE_ARTICLE = 5
        const val VIEW_TYPE_SOUND_CLOUD_MUSIC = 6
    }
}