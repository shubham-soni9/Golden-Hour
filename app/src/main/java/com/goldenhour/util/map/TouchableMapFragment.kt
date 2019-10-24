package com.goldenhour.util.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.SupportMapFragment

/**
 * Provide Custom SupportMapFragment for touchable map
 */
class TouchableMapFragment : SupportMapFragment() {

    private var mOriginalContentView: View? = null
    private var mTouchView: TouchableWrapper? = null

    fun setTouchListener(onTouchListener: TouchableWrapper.OnTouchListener) {
        mTouchView!!.setTouchListener(onTouchListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState)
        mTouchView = TouchableWrapper(mOriginalContentView!!.context)
        mTouchView!!.addView(mOriginalContentView)
        return mTouchView
    }

    override fun getView(): View? {
        return mOriginalContentView
    }

}

