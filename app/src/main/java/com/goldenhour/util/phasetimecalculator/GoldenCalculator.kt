package com.goldenhour.util.phasetimecalculator

import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Provide utility classes to calculate Golden Hour
 */
object GoldenCalculator {

    /* method for calculating phasetime */
    fun getPhaseTime(calendar: Calendar, location: LatLng): PhaseTimeModel? {
        val phaseTimeUtils =
            PhaseTimeUtils(
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR),
                location.longitude,
                location.latitude
            )
        val sunrise = phaseTimeUtils.getLocalTimeZone(true, Zenith.OFFICIAL, calendar)
        val sunset = phaseTimeUtils.getLocalTimeZone(false, Zenith.OFFICIAL, calendar)
        var moonrise = sunset + 1
        if (moonrise > 24) {
            moonrise -= 24.0
        }
        var moonset = sunrise - 1
        if (moonset < 0) {
            moonset += 24.0
        }

        val df = DecimalFormat("#.##")
        val phaseTimeModel = PhaseTimeModel()
        val mSunrise = df.format(sunrise)
        phaseTimeModel.sunrise =
            convertTimeToAmPm(mSunrise)
        val mSunset = df.format(sunset)
        phaseTimeModel.sunset =
            convertTimeToAmPm(mSunset)
        val mMoonrise = df.format(moonrise)
        phaseTimeModel.moonrise =
            convertTimeToAmPm(mMoonrise)
        val mMoonset = df.format(moonset)
        phaseTimeModel.moonset =
            convertTimeToAmPm(mMoonset)
        return phaseTimeModel
    }

    fun getCalenderFromDate(current_date: String): Calendar? {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        try {
            cal.time = sdf.parse(current_date)
            return cal
        } catch (e: ParseException) {
            e.printStackTrace()
            return cal
        }
    }

    fun getCurrentDate(calendar: Calendar): String {
        val c = calendar.time
        val df = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        return df.format(c)
    }

    private fun convertTimeToAmPm(time: String): String {
        try {
            val replace = time.replace(".", ":")
            val _24HourSDF = SimpleDateFormat("HH:mm", Locale.getDefault())
            val _12HourSDF = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val _24HourDt = _24HourSDF.parse(replace)
            return _12HourSDF.format(_24HourDt)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "None"
    }
}
