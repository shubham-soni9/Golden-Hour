package com.goldenhour.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {

    /**
     * Method to check for Internet Connection Status
     *
     * @return true, if data connectivity is enabled, false otherwise
     */
    fun isDeviceOnline(context: Context): Boolean {

        var isConnected = false

        try {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectivityManager.activeNetworkInfo
            isConnected = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
            return isConnected

        } catch (e: Exception) {

            verbose("Utils" + ": Internet Connectivity", e.toString())
        }

        return isConnected
    }

}
