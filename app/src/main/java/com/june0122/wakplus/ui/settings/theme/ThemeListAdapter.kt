package com.june0122.wakplus.ui.settings.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.Theme
import com.june0122.wakplus.databinding.ItemThemeBinding
import com.june0122.wakplus.utils.ISEDOL.INE
import com.june0122.wakplus.utils.ISEDOL.JINGBURGER
import com.june0122.wakplus.utils.ISEDOL.VIICHAN
import com.june0122.wakplus.utils.listeners.ThemeClickListener

class ThemeListAdapter(
    private val themeClickListener: ThemeClickListener
) : RecyclerView.Adapter<ThemeViewHolder>() {
    private val themeList = mutableListOf(
        Theme(INE),
        Theme(JINGBURGER),
        Theme(VIICHAN),
    )

    override fun getItemCount(): Int = themeList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false).also {
            return ThemeViewHolder(it, themeClickListener)
        }
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themeList[holder.absoluteAdapterPosition]
        holder.bind(theme)
    }
}