package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entitiy.SnsPlatformEntity

class SnsPlatformTypeConverter {
    @TypeConverter
    fun snsToJson(value: SnsPlatformEntity): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToSns(value: String): SnsPlatformEntity {
        return Gson().fromJson(value, SnsPlatformEntity::class.java)
    }
}