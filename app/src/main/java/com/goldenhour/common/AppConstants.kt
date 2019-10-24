package com.goldenhour.common

/**
 * File for general all constant value
 */
object AppConstants {
    // Defining empty string
    const val EMPTY_STRING = ""

    // Name of Room Database
    const val DB_NAME = "global_hour.db"

    // Minimum movement required on the map to perform geocoding address api hit
    const val MAP_UPDATED_LOCATION_DIFFERENCE = 5

    /**
     * Location Priority of Fused Location Api
     */
    object LocationPriority {
        const val LOW = 0
        const val BALANCED = 1
        const val BEST = 2
    }
}
