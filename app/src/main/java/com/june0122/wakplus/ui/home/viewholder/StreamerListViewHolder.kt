package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.june0122.wakplus.data.entitiy.Streamer
import com.june0122.wakplus.databinding.ItemStreamerBinding

class StreamerListViewHolder(binding: ItemStreamerBinding): RecyclerView.ViewHolder(binding.root) {
    private val streamerProfileImageView = binding.imgStreamerProfile
    private val streamerNameTextView = binding.tvStreamerName

    fun bind(streamer: Streamer) {
        streamerProfileImageView.load(streamer.profileUrl) {
            scale(Scale.FIT)
            crossfade(true)
            crossfade(300)
            transformations(CircleCropTransformation())
        }

        streamerNameTextView.text = streamer.name
    }
}