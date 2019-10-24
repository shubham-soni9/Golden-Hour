package com.goldenhour.ui.home

import androidx.lifecycle.MutableLiveData
import com.goldenhour.data.local.db.DbHelper
import com.goldenhour.data.model.db.PinnedLocation
import com.goldenhour.ui.base.BaseViewModel
import com.goldenhour.util.phasetimecalculator.GoldenCalculator
import com.google.android.gms.maps.model.LatLng
import java.util.*
import javax.inject.Inject

class HomeViewModel @Inject internal constructor(private val mDbHelper: DbHelper) :
    BaseViewModel() {
    var allDataModel = MutableLiveData<PinnedLocation>()

    init {
        allDataModel.value = PinnedLocation()
        allDataModel.value!!.currentDate = GoldenCalculator.getCurrentDate(Calendar.getInstance())
    }

    private fun updatePhaseTime(location: LatLng, calendar: Calendar?) {
        if (calendar != null) {
            val phaseTimeModel = GoldenCalculator.getPhaseTime(calendar, location)
            val dataModel = PinnedLocation()
            dataModel.sunrise = phaseTimeModel!!.sunrise
            dataModel.sunset = phaseTimeModel.sunset
            dataModel.moonrise = phaseTimeModel.moonrise
            dataModel.moonset = phaseTimeModel.moonset
            dataModel.location = location
            dataModel.address = allDataModel.value!!.address
            dataModel.currentDate = GoldenCalculator.getCurrentDate(calendar)
            allDataModel.value = dataModel
        }
    }

    fun getCurrentLatLng(): LatLng {
        return allDataModel.value!!.location
    }

    fun getCurrentAddress(): String {
        return allDataModel.value!!.address
    }

    fun updateByLocation(latLng: LatLng, address: String) {
        val calendar = GoldenCalculator.getCalenderFromDate(allDataModel.value!!.currentDate)
        allDataModel.value!!.address = address
        updatePhaseTime(latLng, calendar)
    }

    fun incrementDate() {
        val calendar = GoldenCalculator.getCalenderFromDate(allDataModel.value!!.currentDate)
        calendar!!.add(Calendar.DATE, 1)
        updatePhaseTime(allDataModel.value!!.location, calendar)
    }

    fun decrementDate() {
        val calendar = GoldenCalculator.getCalenderFromDate(allDataModel.value!!.currentDate)
        calendar!!.add(Calendar.DATE, -1)
        updatePhaseTime(allDataModel.value!!.location, calendar)
    }

    fun resetDate() {
        updatePhaseTime(allDataModel.value!!.location, Calendar.getInstance())
    }

    suspend fun saveGoldenHour() {
        mDbHelper.insertLocation(allDataModel.value!!)
    }

    fun setCurrentGoldenHour(pinnedLocation: PinnedLocation) {
        allDataModel.value = pinnedLocation
    }
}

