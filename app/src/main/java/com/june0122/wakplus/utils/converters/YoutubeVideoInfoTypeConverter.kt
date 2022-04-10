package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entitiy.YoutubeVideoInfo

class YoutubeVideoInfoTypeConverter {

    @TypeConverter
    fun infoToJson(value: YoutubeVideoInfo): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToInfo(value: String): YoutubeVideoInfo {
        return Gson().fromJson(value, YoutubeVideoInfo::class.java)
    }
}