package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entity.ContentInfo

class ContentInfoTypeConverter {
    @TypeConverter
    fun infoToJson(value: ContentInfo): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToInfo(value: String): ContentInfo {
        return Gson().fromJson(value, ContentInfo::class.java)
    }
}