package com.june0122.wakplus.utils.listeners

import com.june0122.wakplus.data.entity.Content

fun interface FavoriteClickListener {
    fun onFavoriteClick(content: Content)
}