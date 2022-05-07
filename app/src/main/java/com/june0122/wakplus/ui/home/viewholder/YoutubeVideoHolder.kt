package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.Content
import com.june0122.wakplus.databinding.ItemYoutubeVideoBinding
import com.june0122.wakplus.utils.listeners.ContentClickListener
import com.june0122.wakplus.utils.listeners.FavoriteClickListener

class YoutubeVideoHolder(
    private val binding: ItemYoutubeVideoBinding,
    private val favoriteListener: FavoriteClickListener,
    private val contentListener: ContentClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Content) = with(binding) {
        this.content = content
        this.favoriteClickListener = favoriteListener
        this.contentClickListener = contentListener
        executePendingBindings()
    }
}