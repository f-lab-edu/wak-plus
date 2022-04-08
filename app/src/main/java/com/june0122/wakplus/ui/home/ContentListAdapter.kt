package com.june0122.wakplus.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.ContentData
import com.june0122.wakplus.data.entitiy.TwitchVideo
import com.june0122.wakplus.data.entitiy.YoutubeVideo
import com.june0122.wakplus.databinding.ItemTwitchVideoBinding
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding

class ContentListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val contentList = mutableListOf<ContentData>()

    override fun getItemViewType(position: Int): Int {
        return when (contentList[position]) {
            is TwitchVideo -> VIEW_TYPE_TWITCH_VIDEO
            is YoutubeVideo -> VIEW_TYPE_YOUTUBE_VIDEO
            else -> VIEW_TYPE_TWITCH_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TWITCH_VIDEO -> {
                val binding =
                    ItemTwitchVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TwitchVideoHolder(binding)
            }
            VIEW_TYPE_YOUTUBE_VIDEO -> {
                val binding =
                    ItemYoutubeVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YoutubeVideoHolder(binding)
            }
            else -> {
                val tempBinding =
                    ItemTwitchVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TwitchVideoHolder(tempBinding)
            }
        }
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val content = contentList[holder.absoluteAdapterPosition]) {
            is TwitchVideo -> if (holder is TwitchVideoHolder) {
                holder.bind(content.twitchUserInfo, content.twitchVideoInfo)
            }
            is YoutubeVideo -> if (holder is YoutubeVideoHolder) {
                holder.bind(content.youtubeUserInfo, content.youtubeVideoInfo)
            }
            else -> {

            }
        }
    }

    fun updateUserListItems(items: List<ContentData>) {
        val diffCallback = ContentDiffCallback(contentList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        contentList.clear()
        contentList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
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

class ContentDiffCallback(
    private val oldItems: List<ContentData>,
    private val newItems: List<ContentData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return if (oldItem is TwitchVideo && newItem is TwitchVideo) oldItem.twitchVideoInfo.id == newItem.twitchVideoInfo.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem == newItem
    }
}