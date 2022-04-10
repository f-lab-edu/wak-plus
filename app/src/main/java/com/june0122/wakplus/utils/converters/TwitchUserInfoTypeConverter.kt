package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entitiy.TwitchUserInfo

class TwitchUserInfoTypeConverter {

    @TypeConverter
    fun infoToJson(value: TwitchUserInfo): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToInfo(value: String): TwitchUserInfo {
        return Gson().fromJson(value, TwitchUserInfo::class.java)
    }
}