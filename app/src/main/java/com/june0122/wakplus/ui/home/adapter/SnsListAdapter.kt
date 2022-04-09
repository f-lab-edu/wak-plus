package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.SnsPlatform
import com.june0122.wakplus.databinding.ItemSnsPlatformBinding
import com.june0122.wakplus.ui.home.viewholder.SnsListViewHolder

class SnsListAdapter: RecyclerView.Adapter<SnsListViewHolder>() {
    private val snsList = mutableListOf<SnsPlatform>(
        SnsPlatform("전체"),
        SnsPlatform("트위치"),
        SnsPlatform("유튜브"),
        SnsPlatform("인스타그램"),
        SnsPlatform("왁물원"),
        SnsPlatform("트위치"),
        SnsPlatform("사운드클라우드"),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnsListViewHolder {
        ItemSnsPlatformBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
            return SnsListViewHolder(it)
        }
    }

    override fun getItemCount(): Int = snsList.size

    override fun onBindViewHolder(holder: SnsListViewHolder, position: Int) {
        val sns = snsList[holder.absoluteAdapterPosition]
        holder.bind(sns)
    }
}