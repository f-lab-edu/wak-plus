package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.june0122.wakplus.data.entity.StreamerEntity
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.ui.home.viewholder.StreamerListViewHolder
import com.june0122.wakplus.utils.diffcallbacks.StreamerDiffCallback
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class StreamerListAdapter(private val listener: StreamerClickListener) :
    ListAdapter<StreamerEntity, StreamerListViewHolder>(StreamerDiffCallback()) {

    operator fun get(position: Int): StreamerEntity = currentList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamerListViewHolder {
        val binding = ItemStreamerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StreamerListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: StreamerListViewHolder, position: Int) {
        val streamer = currentList[holder.absoluteAdapterPosition]
        holder.bind(streamer)
    }
}