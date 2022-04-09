package com.june0122.wakplus.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entitiy.Streamer
import com.june0122.wakplus.databinding.ItemStreamerBinding
import com.june0122.wakplus.ui.home.viewholder.StreamerListViewHolder

class StreamerListAdapter : RecyclerView.Adapter<StreamerListViewHolder>() {
    private val streamerList = mutableListOf<Streamer>(
        Streamer(
            "https://yt3.ggpht.com/hk4Bg_RBb21e2IDLN_Gjmw0jGfMIh26usUwjBvLr472mX8_l8dednSbifhXKPP0QCN8_EPAWBV0=s800-c-k-c0x00ffffff-no-rj",
            "아이네"
        ),
        Streamer(
            "https://yt3.ggpht.com/ZFF_hEJhjNyF3UJLolZZPEV8EMM7V-e8HtTvzLiZXNM6s4rh518242ghR-bUXRYkMaJtedKoaZA=s800-c-k-c0x00ffffff-no-rj",
            "릴파"
        ),
        Streamer(
            "https://yt3.ggpht.com/mgeSP-KxZvBEtEVYYGyWeiTJ7C1ap1ZwGYM2Dfew7tYh6maJV0CJYf_OIASeUKVJmFMVcZE-BQ=s800-c-k-c0x00ffffff-no-rj",
            "비챤"
        ),
        Streamer(
            "https://yt3.ggpht.com/v3a75a7gUHU6E-gaJww_k5gkFYI8jthCtAR9ELMaRemymZhIyQLiIIRu4cWOt289DFH1UNkFMA=s800-c-k-c0x00ffffff-no-rj",
            "주르르"
        ),
        Streamer(
            "https://yt3.ggpht.com/5vwZ3NZL6Zv4C7cl5sshsTk-XycH7r-4zo6nQR7g9Z7SLrMzeabWWzn5M1V3SqJXjTxLj_hb=s800-c-k-c0x00ffffff-no-rj",
            "징버거"
        ),
        Streamer(
            "https://yt3.ggpht.com/AIoO_0IdKYBdzlcRQ85oZxMaTBj_RVDvP8QmTmJZoOO_TTJd5NXql17hDfIl_bvcTQ4aAqFGIA=s800-c-k-c0x00ffffff-no-rj",
            "고세구"
        ),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamerListViewHolder {
        ItemStreamerBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
            return StreamerListViewHolder(it)
        }
    }

    override fun getItemCount(): Int = streamerList.size

    override fun onBindViewHolder(holder: StreamerListViewHolder, position: Int) {
        val streamer = streamerList[holder.absoluteAdapterPosition]
        holder.bind(streamer)
    }
}