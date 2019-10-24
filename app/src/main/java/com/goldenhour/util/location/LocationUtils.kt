package com.goldenhour.util.location

import android.content.Context
import android.location.LocationManager

/**
 * Common location utility methods
 */
object LocationUtils {

    /**
     * Method to check whether location services
     * through network are enabled or not
     */
    private fun isProviderEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            return false
        }

    }

    /**
     * Method to check whether the GPS is enabled or not
     *
     * @param context
     * @return
     */
    fun isGPSEnabled(context: Context): Boolean {
        return isProviderEnabled(context)
    }

}
