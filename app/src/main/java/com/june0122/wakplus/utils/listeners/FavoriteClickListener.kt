package com.june0122.wakplus.utils.listeners

import com.june0122.wakplus.data.entitiy.ContentData

fun interface FavoriteClickListener {
    fun onFavoriteClick(content: ContentData)
}