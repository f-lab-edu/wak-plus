package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entitiy.TwitchVideoInfo

class TwitchVideoInfoTypeConverter {

    @TypeConverter
    fun infoToJson(value: TwitchVideoInfo): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToInfo(value: String): TwitchVideoInfo {
        return Gson().fromJson(value, TwitchVideoInfo::class.java)
    }
}