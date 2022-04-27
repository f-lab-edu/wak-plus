package com.june0122.wakplus.utils.listeners

import com.june0122.wakplus.data.entity.ContentEntity

fun interface FavoriteClickListener {
    fun onFavoriteClick(content: ContentEntity)
}