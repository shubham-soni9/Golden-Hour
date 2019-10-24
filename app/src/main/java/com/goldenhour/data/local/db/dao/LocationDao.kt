package com.goldenhour.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goldenhour.data.model.db.PinnedLocation

/**
 * Dao for saving location in room database
 */
@Dao
interface LocationDao {

    // Inserting the saved location into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pinnedLocation: PinnedLocation)

    // Getting all saved location from database
    @Query("SELECT * FROM pinned_location")
    fun loadAll(): LiveData<List<PinnedLocation>>
}
