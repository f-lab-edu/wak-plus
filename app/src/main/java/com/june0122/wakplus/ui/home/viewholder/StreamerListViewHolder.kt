package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class StreamerListViewHolder(private val binding: ItemStreamerBinding, private val listener: StreamerClickListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(streamer: StreamerEntity) = with(binding) {
        this.streamer = streamer
        this.streamerClickListener = listener
        this.position = absoluteAdapterPosition
        executePendingBindings()
    }
}