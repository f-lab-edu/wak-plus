package com.june0122.wakplus.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.SnsPlatform
import com.june0122.wakplus.databinding.ItemSnsPlatformBinding

class SnsListViewHolder(binding: ItemSnsPlatformBinding): RecyclerView.ViewHolder(binding.root) {
    val snsChip = binding.chipSns

    fun bind(snsPlatform: SnsPlatform) {
        snsChip.text = snsPlatform.serviceName
    }
}