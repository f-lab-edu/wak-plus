package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.SnsPlatform
import com.june0122.wakplus.databinding.ItemSnsPlatformBinding

class SnsListViewHolder(private val binding: ItemSnsPlatformBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(snsPlatform: SnsPlatform) = with(binding) {
        chipSns.text = snsPlatform.serviceName
    }
}