package com.june0122.wakplus.utils.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.june0122.wakplus.data.entitiy.StreamerEntity

class StreamerDiffCallback : DiffUtil.ItemCallback<StreamerEntity>() {
    override fun areItemsTheSame(oldItem: StreamerEntity, newItem: StreamerEntity): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: StreamerEntity, newItem: StreamerEntity): Boolean {
        return oldItem == newItem
    }
}