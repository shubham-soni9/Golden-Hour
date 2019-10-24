package com.goldenhour.util

import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.TypedValue
import android.widget.Toast
import com.goldenhour.R
import com.goldenhour.common.AppConstants.EMPTY_STRING
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import es.dmoral.toasty.Toasty

/**
 * General Utility Methods
 */
object Utils {

    fun searchPlace(activity: Activity, requestCode: Int) {
        try {
            if (!Places.isInitialized()) {
                Places.initialize(activity, activity.getString(R.string.google_maps_key))
            }
            val fieldList = arrayListOf<Place.Field>()
            fieldList.add(Place.Field.ADDRESS)
            fieldList.add(Place.Field.LAT_LNG)
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList)
                    .build(activity)
            activity.startActivityForResult(intent, requestCode)
        } catch (e: GooglePlayServicesRepairableException) {
            Toasty.error(
                activity,
                activity.getString(R.string.google_play_services_error),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            Toasty.error(
                activity,
                activity.getString(R.string.play_service_required),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getDistance(LatLng1: LatLng, LatLng2: LatLng): Float {
        val locationA = Location("A")
        locationA.latitude = LatLng1.latitude
        locationA.longitude = LatLng1.longitude
        val locationB = Location("B")
        locationB.latitude = LatLng2.latitude
        locationB.longitude = LatLng2.longitude
        return locationA.distanceTo(locationB)
    }

    fun dpToPx(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    /**
     * Method to assign Strings safely
     */
    fun assign(assignable: String, alternative: String): String {
        return when {
            assignable.isBlank() -> assign(alternative)
            else -> assignable
        }
    }

    /**
     * Method to assign strings Safely
     */
    fun assign(assignable: String): String {
        return when {
            assignable.isBlank() -> EMPTY_STRING
            else -> assignable
        }
    }
}

