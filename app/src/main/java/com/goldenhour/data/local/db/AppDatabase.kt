package com.goldenhour.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

import com.goldenhour.data.local.db.dao.LocationDao
import com.goldenhour.data.model.db.PinnedLocation

/**
 * Database defination for define tables and Dao
 */
@Database(entities = [PinnedLocation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Providing Dao of location database
    abstract fun locationDao(): LocationDao
}
