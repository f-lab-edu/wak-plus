package com.june0122.wakplus.data.entitiy

sealed class ContentData

data class Streamer(
    val profileUrl: String,
    val name: String,
)

data class SnsPlatform(
    val serviceName: String,
)