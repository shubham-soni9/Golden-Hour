package com.goldenhour.data.local.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.goldenhour.data.model.db.PinnedLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Providing Database helper of accessing room database tables
 */
@Singleton
class AppDbHelper @Inject
constructor(private val mAppDatabase: AppDatabase) : DbHelper {
    override fun getAllLocation(): LiveData<List<PinnedLocation>> {
        return mAppDatabase.locationDao().loadAll()
    }

    @WorkerThread
    override suspend fun insertLocation(pinnedLocation: PinnedLocation) =
        withContext(Dispatchers.IO) {
            mAppDatabase.locationDao().insert(pinnedLocation)
        }
}
