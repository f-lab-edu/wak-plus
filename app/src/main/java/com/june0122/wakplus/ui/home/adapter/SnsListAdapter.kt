package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.databinding.ItemSnsPlatformBinding
import com.june0122.wakplus.ui.home.viewholder.SnsListViewHolder
import com.june0122.wakplus.utils.diffcallbacks.SnsDiffCallback
import com.june0122.wakplus.utils.listeners.SnsClickListener

class SnsListAdapter(
    private val listener: SnsClickListener
) : ListAdapter<SnsPlatformEntity, SnsListViewHolder>(SnsDiffCallback()) {

    operator fun get(position: Int): SnsPlatformEntity = currentList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnsListViewHolder {
        ItemSnsPlatformBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
            return SnsListViewHolder(it, listener)
        }
    }

    override fun onBindViewHolder(holder: SnsListViewHolder, position: Int) {
        val sns = currentList[holder.absoluteAdapterPosition]
        holder.bind(sns)
    }
}