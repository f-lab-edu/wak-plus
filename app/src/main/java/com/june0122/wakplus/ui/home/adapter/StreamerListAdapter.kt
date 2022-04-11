package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.R
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.ui.home.viewholder.StreamerListViewHolder
import com.june0122.wakplus.utils.DiffCallback
import com.june0122.wakplus.utils.listeners.StreamerClickListener

class StreamerListAdapter(private val listener: StreamerClickListener) :
    RecyclerView.Adapter<StreamerListViewHolder>() {
    private val streamerList = mutableListOf<StreamerEntity>()
    private var previousSelectedPosition = DEFAULT_POS
    var selectedPosition = UNSELECTED

    operator fun get(position: Int): StreamerEntity = streamerList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamerListViewHolder {
        ItemStreamerBinding.inflate(LayoutInflater.from(parent.context), parent, false).also { binding ->
            return StreamerListViewHolder(binding, listener)
        }
    }

    override fun getItemCount(): Int = streamerList.size

    override fun onBindViewHolder(holder: StreamerListViewHolder, position: Int) {
        val streamer = streamerList[holder.absoluteAdapterPosition]
        holder.bind(streamer)

        if (selectedPosition == position && selectedPosition != previousSelectedPosition) {
            holder.itemView.setBackgroundResource(R.color.Primary50)
        } else if (selectedPosition == position && selectedPosition == previousSelectedPosition) {
            holder.itemView.setBackgroundResource(R.color.transparent)
            selectedPosition = UNSELECTED
        } else if (selectedPosition != position) {
            holder.itemView.setBackgroundResource(R.color.transparent)
        }
    }

    fun updateStreamerListItems(items: List<StreamerEntity>) {
        val diffCallback = object : DiffCallback<StreamerEntity>(streamerList, items) {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldItems[oldItemPosition]
                val newItem = newItems[newItemPosition]
                return oldItem.name == newItem.name
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        streamerList.clear()
        streamerList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun selectSinglePosition(adapterPosition: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedPosition)
        previousSelectedPosition = selectedPosition
        selectedPosition = adapterPosition
        notifyItemChanged(selectedPosition)
    }

    companion object {
        const val UNSELECTED = -1
        const val DEFAULT_POS = -2
    }
}