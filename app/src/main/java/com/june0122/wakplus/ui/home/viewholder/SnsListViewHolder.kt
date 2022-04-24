package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.databinding.ItemSnsPlatformBinding
import com.june0122.wakplus.utils.listeners.SnsClickListener

class SnsListViewHolder(
    private val binding: ItemSnsPlatformBinding,
    private val listener: SnsClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(snsPlatform: SnsPlatformEntity) = with(binding) {
        this.snsPlatform = snsPlatform
        this.snsClickListener = listener
        this.position = absoluteAdapterPosition
        executePendingBindings()
    }
}