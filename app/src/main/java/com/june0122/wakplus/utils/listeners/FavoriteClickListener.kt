package com.june0122.wakplus.utils.listeners

import com.june0122.wakplus.data.entity.TwitchVideoEntity

fun interface FavoriteClickListener {
    fun onFavoriteClick(favoriteContent: TwitchVideoEntity)
}