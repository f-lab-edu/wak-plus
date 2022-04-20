package com.june0122.wakplus.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.june0122.wakplus.data.entitiy.IdSet

class IdSetTypeConverter {
    @TypeConverter
    fun setToJson(value: IdSet): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToSet(value: String): IdSet {
        return Gson().fromJson(value, IdSet::class.java)
    }
}