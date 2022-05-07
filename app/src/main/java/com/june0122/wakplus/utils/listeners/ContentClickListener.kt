package com.june0122.wakplus.utils.listeners

import com.june0122.wakplus.data.entity.Content

fun interface ContentClickListener {
    fun onContentClick(url: String, content: Content)
}