package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.databinding.ItemTwitchVideoBinding
import com.june0122.wakplus.utils.listeners.ContentClickListener
import com.june0122.wakplus.utils.listeners.FavoriteClickListener
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class TwitchVideoHolder(
    private val binding: ItemTwitchVideoBinding,
    private val favoriteListener: FavoriteClickListener,
    private val contentListener: ContentClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Content) = with(binding) {
        this.content = content
        this.favoriteClickListener = favoriteListener
        this.contentClickListener = contentListener
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