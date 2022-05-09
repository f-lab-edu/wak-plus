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
import com.june0122.wakplus.utils.SNS
import com.june0122.wakplus.utils.diffcallbacks.ContentDiffCallback
import com.june0122.wakplus.utils.listeners.ContentClickListener
import com.june0122.wakplus.utils.listeners.FavoriteClickListener

class ContentListAdapter(
    private val favoriteListener: FavoriteClickListener,
    private val contentListener: ContentClickListener,
) : ListAdapter<Content, RecyclerView.ViewHolder>(ContentDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        currentList[position].contentType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SNS.TWITCH -> {
                val binding =
                    ItemTwitchVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TwitchVideoHolder(binding, favoriteListener, contentListener)
            }
            SNS.YOUTUBE -> {
                val binding =
                    ItemYoutubeVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YoutubeVideoHolder(binding, favoriteListener, contentListener)
            }
            else -> {
                val tempBinding =
                    ItemTwitchVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TwitchVideoHolder(tempBinding, favoriteListener, contentListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val content = currentList[holder.absoluteAdapterPosition]
        when (content.contentType) {
            SNS.TWITCH -> if (holder is TwitchVideoHolder) {
                holder.bind(content)
            }
            SNS.YOUTUBE -> if (holder is YoutubeVideoHolder) {
                holder.bind(content)
            }
        }
    }
}