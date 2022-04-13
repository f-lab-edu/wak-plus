package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class StreamerListViewHolder(private val binding: ItemStreamerBinding, listener: StreamerClickListener) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener { listener.onStreamerClick(absoluteAdapterPosition) }
        itemView.setOnLongClickListener {
            listener.onStreamerLongClick(absoluteAdapterPosition)
            return@setOnLongClickListener true
        }

        binding.layoutStreamer.setOnClickListener {
            listener.onStreamerClick(absoluteAdapterPosition)
        }
    }

    fun bind(streamer: StreamerEntity) = with(binding) {
        imgStreamerProfile.load(streamer.profileUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }

        tvStreamerName.text = streamer.name

        if (streamer.isSelected) layoutStreamer.setBackgroundResource(R.color.Primary50)
        else layoutStreamer.setBackgroundResource(R.color.transparent)
    }
}