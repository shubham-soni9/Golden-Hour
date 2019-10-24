package com.goldenhour.ui.home.dialog

import com.goldenhour.data.model.db.PinnedLocation

/**
 * Listener for getting events from SavedPinDialog
 */
interface OnSavedLocationListener {
    fun onSavedLocationClicked(pinnedLocation: PinnedLocation)
}
