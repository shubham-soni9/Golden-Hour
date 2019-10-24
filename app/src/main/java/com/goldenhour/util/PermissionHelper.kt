package com.goldenhour.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.goldenhour.R
import com.goldenhour.common.Codes
import com.goldenhour.util.dialog.AlertDialog

/**
 * Utility class for getting runtime permission from user
 */
open class PermissionHelper(context: Context) {
    val context: Activity = context as Activity
    private val permissionCallbacks: PermissionCallbacks = context as PermissionCallbacks

    fun requestLocationPermissions(isForce: Boolean) {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        var permissionGranted = true
        var permissionDenied = ""
        for (permission in permissions)
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionDenied = permission
                permissionGranted = false
                break
            }

        if (permissionGranted) {
            permissionCallbacks.onPermissionGranted()
        } else {
            val explanationRequired =
                ActivityCompat.shouldShowRequestPermissionRationale(context, permissionDenied)
            if (explanationRequired && !isForce) {
                AlertDialog.Builder(context).message(R.string.rationale_ask).button(R.string.ok)
                    .listener(object : AlertDialog.Listener {
                        override fun performPostAlertAction(purpose: Int, backpack: Bundle?) {
                            requestPermissions(
                                permissions,
                                Codes.REQ_LOCATION_PERMISSION
                            )
                        }
                    })
                    .build().show()
            } else {
                requestPermissions(
                    permissions,
                    Codes.REQ_LOCATION_PERMISSION
                )
            }
        }
    }

    fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(
            context,
            permissions,
            requestCode
        )
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var isAllGranted = true
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false
                break
            }
        }
        if (isAllGranted) {
            permissionCallbacks.onPermissionGranted()
        } else {
            permissionCallbacks.onPermissionRejected()
        }
    }
}