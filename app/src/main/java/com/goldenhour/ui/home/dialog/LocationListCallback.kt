package com.goldenhour.ui.home.dialog

import com.goldenhour.data.model.db.PinnedLocation

/**
 * Callback for location list
 */
interface LocationListCallback {
    fun onLocationClicked(pinnedLocation: PinnedLocation)
}
