package com.june0122.wakplus.data.room

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.june0122.wakplus.data.entitiy.StreamerEntity
import com.june0122.wakplus.data.room.ContentRoomDatabase.Companion.INSTANCE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        INSTANCE?.let { database ->
            scope.launch(Dispatchers.IO) {
                populateDatabase(database.contentDao())
            }
        }
    }

    private suspend fun populateDatabase(contentDao: ContentDao) {
        val streamerList = mutableListOf(
            StreamerEntity(
                "아이네",
                "https://yt3.ggpht.com/hk4Bg_RBb21e2IDLN_Gjmw0jGfMIh26usUwjBvLr472mX8_l8dednSbifhXKPP0QCN8_EPAWBV0=s800-c-k-c0x00ffffff-no-rj",
            ),
            StreamerEntity(
                "릴파",
                "https://yt3.ggpht.com/ZFF_hEJhjNyF3UJLolZZPEV8EMM7V-e8HtTvzLiZXNM6s4rh518242ghR-bUXRYkMaJtedKoaZA=s800-c-k-c0x00ffffff-no-rj",
            ),
            StreamerEntity(
                "비챤",
                "https://yt3.ggpht.com/mgeSP-KxZvBEtEVYYGyWeiTJ7C1ap1ZwGYM2Dfew7tYh6maJV0CJYf_OIASeUKVJmFMVcZE-BQ=s800-c-k-c0x00ffffff-no-rj",
            ),
            StreamerEntity(
                "주르르",
                "https://yt3.ggpht.com/v3a75a7gUHU6E-gaJww_k5gkFYI8jthCtAR9ELMaRemymZhIyQLiIIRu4cWOt289DFH1UNkFMA=s800-c-k-c0x00ffffff-no-rj",
            ),
            StreamerEntity(
                "징버거",
                "https://yt3.ggpht.com/5vwZ3NZL6Zv4C7cl5sshsTk-XycH7r-4zo6nQR7g9Z7SLrMzeabWWzn5M1V3SqJXjTxLj_hb=s800-c-k-c0x00ffffff-no-rj",
            ),
            StreamerEntity(
                "고세구",
                "https://yt3.ggpht.com/AIoO_0IdKYBdzlcRQ85oZxMaTBj_RVDvP8QmTmJZoOO_TTJd5NXql17hDfIl_bvcTQ4aAqFGIA=s800-c-k-c0x00ffffff-no-rj",
            ),
        )

        streamerList.map { streamer -> contentDao.insertStreamer(streamer) }
    }
}