package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entitiy.YoutubeUserInfo

class YotubeUserInfoTypeConverter {

    @TypeConverter
    fun infoToJson(value: YoutubeUserInfo): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToInfo(value: String): YoutubeUserInfo {
        return Gson().fromJson(value, YoutubeUserInfo::class.java)
    }
}