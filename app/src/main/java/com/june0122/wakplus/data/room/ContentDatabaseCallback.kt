package com.june0122.wakplus.data.room

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.june0122.wakplus.data.entity.IdSet
import com.june0122.wakplus.data.entity.SnsPlatformEntity
import com.june0122.wakplus.data.entity.StreamerEntity
import com.june0122.wakplus.data.room.ContentRoomDatabase.Companion.INSTANCE
import com.june0122.wakplus.utils.SNS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContentDatabaseCallback @Inject constructor(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        INSTANCE?.let { database ->
            scope.launch(Dispatchers.IO) {
                populateDatabase(database.contentDao())
            }
        }
    }

    private suspend fun populateDatabase(contentDao: ContentDao) {
        // TODO: JSON 파일로 Default Data 저장하기
        val streamerList = mutableListOf(
            StreamerEntity(
                "아이네",
                "https://yt3.ggpht.com/hk4Bg_RBb21e2IDLN_Gjmw0jGfMIh26usUwjBvLr472mX8_l8dednSbifhXKPP0QCN8_EPAWBV0=s800-c-k-c0x00ffffff-no-rj",
                IdSet("702754423", "UCroM00J2ahCN6k-0-oAiDxg"),
                false
            ),
            StreamerEntity(
                "릴파",
                "https://yt3.ggpht.com/ZFF_hEJhjNyF3UJLolZZPEV8EMM7V-e8HtTvzLiZXNM6s4rh518242ghR-bUXRYkMaJtedKoaZA=s800-c-k-c0x00ffffff-no-rj",
                IdSet("169700336", "UC-oCJP9t47v7-DmsnmXV38Q"),
                false
            ),
            StreamerEntity(
                "비챤",
                "https://yt3.ggpht.com/mgeSP-KxZvBEtEVYYGyWeiTJ7C1ap1ZwGYM2Dfew7tYh6maJV0CJYf_OIASeUKVJmFMVcZE-BQ=s800-c-k-c0x00ffffff-no-rj",
                IdSet("195641865", "UCs6EwgxKLY9GG4QNUrP5hoQ"),
                false
            ),
            StreamerEntity(
                "주르르",
                "https://yt3.ggpht.com/v3a75a7gUHU6E-gaJww_k5gkFYI8jthCtAR9ELMaRemymZhIyQLiIIRu4cWOt289DFH1UNkFMA=s800-c-k-c0x00ffffff-no-rj",
                IdSet("203667951", "UCTifMx1ONpElK5x6B4ng8eg"),
                false
            ),
            StreamerEntity(
                "징버거",
                "https://yt3.ggpht.com/5vwZ3NZL6Zv4C7cl5sshsTk-XycH7r-4zo6nQR7g9Z7SLrMzeabWWzn5M1V3SqJXjTxLj_hb=s800-c-k-c0x00ffffff-no-rj",
                IdSet("237570548", "UCHE7GBQVtdh-c1m3tjFdevQ"),
                false
            ),
            StreamerEntity(
                "고세구",
                "https://yt3.ggpht.com/AIoO_0IdKYBdzlcRQ85oZxMaTBj_RVDvP8QmTmJZoOO_TTJd5NXql17hDfIl_bvcTQ4aAqFGIA=s800-c-k-c0x00ffffff-no-rj",
                IdSet("707328484", "UCV9WL7sW6_KjanYkUUaIDfQ"),
                false
            ),
        )

        streamerList.forEach { streamer -> contentDao.insertStreamer(streamer) }

        val snsList = mutableListOf(
            SnsPlatformEntity(SNS.ALL.ordinal, true), // Default Selected Sns Platform
            SnsPlatformEntity(SNS.TWITCH.ordinal, false),
            SnsPlatformEntity(SNS.YOUTUBE.ordinal, false),
            SnsPlatformEntity(SNS.INSTAGRAM.ordinal, false),
            SnsPlatformEntity(SNS.NAVER_CAFE.ordinal, false),
            SnsPlatformEntity(SNS.TWITTER.ordinal, false),
            SnsPlatformEntity(SNS.SOUNDCLOUD.ordinal, false),
        )

        snsList.forEach { sns -> contentDao.insertSnsPlatform(sns) }
    }
}