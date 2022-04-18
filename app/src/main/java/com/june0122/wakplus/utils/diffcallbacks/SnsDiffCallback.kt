package com.june0122.wakplus.utils.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.june0122.wakplus.data.entitiy.SnsPlatformEntity

class SnsDiffCallback : DiffUtil.ItemCallback<SnsPlatformEntity>() {
    override fun areItemsTheSame(oldItem: SnsPlatformEntity, newItem: SnsPlatformEntity): Boolean {
        return oldItem.serviceName == newItem.serviceName
    }

    override fun areContentsTheSame(oldItem: SnsPlatformEntity, newItem: SnsPlatformEntity): Boolean {
        return oldItem == newItem
    }
}