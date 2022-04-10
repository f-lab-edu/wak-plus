package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class StreamerListViewHolder(binding: ItemStreamerBinding, listener: StreamerClickListener) :
    RecyclerView.ViewHolder(binding.root) {
    private val streamerLayout = binding.layoutStreamer
    private val streamerProfileImageView = binding.imgStreamerProfile
    private val streamerNameTextView = binding.tvStreamerName

    init {
        itemView.setOnClickListener { listener.onStreamerClick(absoluteAdapterPosition) }
        itemView.setOnLongClickListener {
            listener.onStreamerLongClick(absoluteAdapterPosition)
            return@setOnLongClickListener true
        }

        streamerLayout.setOnClickListener {
            listener.onStreamerClick(absoluteAdapterPosition)
            if (streamerLayout.isSelected) {
                streamerLayout.setBackgroundResource(R.color.transparent)
                streamerLayout.isSelected = false
            } else {
                streamerLayout.setBackgroundResource(R.color.Primary50)
                streamerLayout.isSelected = true
            }
        }
    }

    fun bind(streamer: StreamerEntity) {
        streamerProfileImageView.load(streamer.profileUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }

        streamerNameTextView.text = streamer.name
    }
}