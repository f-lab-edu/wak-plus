package com.june0122.wakplus.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.june0122.wakplus.data.entity.*
import com.june0122.wakplus.utils.converters.*
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        Content::class,
        StreamerEntity::class,
        SnsPlatformEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        ContentInfoTypeConverter::class,
        IdSetTypeConverter::class,
        SnsPlatformTypeConverter::class,
    ]
)
abstract class ContentRoomDatabase : RoomDatabase() {

    abstract fun contentDao(): ContentDao

    companion object {
        @Volatile
        var INSTANCE: ContentRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ContentRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContentRoomDatabase::class.java,
                    "content_database"
                )
                    .addCallback(ContentDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}