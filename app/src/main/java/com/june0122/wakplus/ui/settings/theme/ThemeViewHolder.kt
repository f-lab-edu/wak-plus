package com.june0122.wakplus.ui.settings.theme

import androidx.recyclerview.widget.RecyclerView
import com.june0122.wakplus.data.entity.Theme
import com.june0122.wakplus.databinding.ItemThemeBinding
import com.june0122.wakplus.utils.listeners.ThemeClickListener

class ThemeViewHolder(
    private val binding: ItemThemeBinding,
    private val listener: ThemeClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(theme: Theme) = with(binding) {
        this.theme = theme
        this.themeClickListener = listener
    }
}