package com.goldenhour.util.map

import android.app.Activity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import java.util.*

/**
 * This class provides the neccessary callback during touching google map
 */
abstract class MapStateListener(
    private val mMap: GoogleMap,
    touchableMapFragment: TouchableMapFragment,
    private val mActivity: Activity
) {
    private val _settleTime = 500
    private var mMapTouched = false
    private var mMapSettled = false
    private var mTimer: Timer? = null
    private var mLastPosition: CameraPosition? = null

    init {
        mMap.setOnCameraMoveStartedListener {
            unsettleMap()
            if (!mMapTouched) {
                runSettleTimer()
            }
        }

        touchableMapFragment.setTouchListener(object : TouchableWrapper.OnTouchListener {
            override fun onTouch() {
                touchMap()
                unsettleMap()
            }

            override fun onRelease() {
                releaseMap()
                runSettleTimer()
            }

            override fun onDoubleTap() {
                mMap.animateCamera(CameraUpdateFactory.zoomIn())
            }

            override fun onTwoFingerDoubleTap() {
                mMap.animateCamera(CameraUpdateFactory.zoomOut())
            }

            override fun pinchIn() {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.cameraPosition.zoom - 0.04f))
            }

            override fun pinchOut() {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.cameraPosition.zoom + 0.04f))
            }

        })
    }

    private fun updateLastPosition() {
        mActivity.runOnUiThread { mLastPosition = this@MapStateListener.mMap.cameraPosition }
    }

    private fun runSettleTimer() {
        updateLastPosition()

        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer!!.purge()
        }
        mTimer = Timer()
        mTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mActivity.runOnUiThread {
                    val currentPosition = this@MapStateListener.mMap.cameraPosition
                    if (currentPosition == mLastPosition) {
                        settleMap()
                    }
                }
            }
        }, _settleTime.toLong())
    }

    @Synchronized
    private fun releaseMap() {
        if (mMapTouched) {
            mMapTouched = false
            onMapReleased()
        }
    }

    private fun touchMap() {
        if (!mMapTouched) {
            if (mTimer != null) {
                mTimer!!.cancel()
                mTimer!!.purge()
            }
            mMapTouched = true
            onMapTouched()
        }
    }

    private fun unsettleMap() {
        if (mMapSettled) {
            if (mTimer != null) {
                mTimer!!.cancel()
                mTimer!!.purge()
            }
            mMapSettled = false
            mLastPosition = null
            onMapUnsettled()
        }
    }

    private fun settleMap() {
        if (!mMapSettled) {
            mMapSettled = true
            onMapSettled()
        }
    }

    protected abstract fun onMapTouched()

    protected abstract fun onMapReleased()

    protected abstract fun onMapUnsettled()

    protected abstract fun onMapSettled()

}

