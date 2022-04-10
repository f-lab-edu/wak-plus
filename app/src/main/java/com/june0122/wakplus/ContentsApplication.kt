package com.june0122.wakplus

import android.app.Application
import com.june0122.wakplus.data.repository.ContentRepository
import com.june0122.wakplus.data.room.ContentRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ContentsApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ContentRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ContentRepository(database.contentDao()) }
}