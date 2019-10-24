package com.goldenhour.ui.home.dialog

import androidx.lifecycle.LiveData
import com.goldenhour.data.local.db.DbHelper
import com.goldenhour.data.model.db.PinnedLocation
import com.goldenhour.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * ViewModel for Defining item in saved location list dialog
 */
class SavedPinViewModel @Inject internal constructor(private val mDbHelper: DbHelper) :
    BaseViewModel() {

    fun getAllLocation(): LiveData<List<PinnedLocation>> {
        return mDbHelper.getAllLocation()
    }
}
