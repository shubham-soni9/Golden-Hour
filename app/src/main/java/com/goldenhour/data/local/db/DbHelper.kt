package com.goldenhour.data.local.db

import androidx.lifecycle.LiveData
import com.goldenhour.data.model.db.PinnedLocation

/**
 * Providing Interface for database helper of accessing room database tables
 */
interface DbHelper {
    fun getAllLocation(): LiveData<List<PinnedLocation>>
    suspend fun insertLocation(pinnedLocation: PinnedLocation)
}
