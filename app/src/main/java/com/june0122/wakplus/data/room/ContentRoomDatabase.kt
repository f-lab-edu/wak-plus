package com.june0122.wakplus.data.room

import android.content.Context
import androidx.room.*
import com.june0122.wakplus.data.entitiy.*
import com.june0122.wakplus.utils.converters.*
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        StreamerEntity::class,
        TwitchVideoEntity::class,
        YoutubeVideoEntity::class,
        SnsPlatformEntity::class],
    version = 1
)
@TypeConverters(
    value = [
        IdSetTypeConverter::class,
        TwitchUserInfoTypeConverter::class,
        TwitchVideoInfoTypeConverter::class,
        YotubeUserInfoTypeConverter::class,
        YoutubeVideoInfoTypeConverter::class,
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