package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.ui.home.viewholder.StreamerListViewHolder
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class StreamerListAdapter(private val listener: StreamerClickListener) : RecyclerView.Adapter<StreamerListViewHolder>() {
    private val streamerList = mutableListOf<StreamerEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamerListViewHolder {
        ItemStreamerBinding.inflate(LayoutInflater.from(parent.context), parent, false).also { binding ->
            return StreamerListViewHolder(binding, listener)
        }
    }

    override fun getItemCount(): Int = streamerList.size

    override fun onBindViewHolder(holder: StreamerListViewHolder, position: Int) {
        val streamer = streamerList[holder.absoluteAdapterPosition]
        holder.bind(streamer)
    }

    fun updateStreamerListItems(items: List<StreamerEntity>) {
        val diffCallback = StreamerDiffCallback(streamerList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        streamerList.clear()
        streamerList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}

class StreamerDiffCallback(
    private val oldItems: List<StreamerEntity>,
    private val newItems: List<StreamerEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem == newItem
    }
}