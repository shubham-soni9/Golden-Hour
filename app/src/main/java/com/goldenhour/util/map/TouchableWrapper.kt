package com.goldenhour.util.map

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * This class provide wrapper over touch event in google map
 */
class TouchableWrapper(context: Context) : FrameLayout(context) {

    private var onTouchListener: OnTouchListener? = null
    private val gestureDetector: GestureDetector
    private val twoFingerTapDetector: TwoFingerTapDetector
    private val scaleGestureDetector: ScaleGestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())

        twoFingerTapDetector = object : TwoFingerTapDetector() {
            public override fun onTwoFingerDoubleTap() {
                onTouchListener!!.onTwoFingerDoubleTap()
            }
        }
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    fun setTouchListener(onTouchListener: OnTouchListener) {
        this.onTouchListener = onTouchListener
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        try {
            if (event == null) return super.dispatchTouchEvent(event)

            gestureDetector.onTouchEvent(event)
            twoFingerTapDetector.onTouchEvent(event)
            scaleGestureDetector.onTouchEvent(event)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (onTouchListener != null)
                    onTouchListener!!.onTouch()
                MotionEvent.ACTION_MOVE -> return if (event.pointerCount >= 2) {
                    true
                } else {
                    super.dispatchTouchEvent(event)
                }
                MotionEvent.ACTION_UP -> if (onTouchListener != null)
                    onTouchListener!!.onRelease()
            }

            return super.dispatchTouchEvent(event)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return false
        }

    }

    interface OnTouchListener {
        fun onTouch()

        fun onRelease()

        fun onDoubleTap()

        fun onTwoFingerDoubleTap()

        fun pinchIn()

        fun pinchOut()
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent): Boolean {
            onTouchListener!!.onDoubleTap()
            return true
        }

    }

    internal inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        private var factor = 1f
        private val threshold = 0.000001f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            if (scaleFactor > 1) {
                //Zoom in
                if (abs(scaleFactor - factor) > threshold) {
                    onTouchListener!!.pinchOut()
                }
            } else {
                //Zoom out
                if (abs(scaleFactor - factor) > threshold) {
                    onTouchListener!!.pinchIn()
                }
            }
            factor = scaleFactor
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            factor = detector.scaleFactor
            return super.onScaleBegin(detector)
        }

    }

    internal abstract inner class TwoFingerTapDetector {

        private var moved = false
        private var threshold = 20f
        private var pixX: Float = 0.toFloat()
        private var pixY: Float = 0.toFloat()

        fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    moved = false
                    pixX = event.x
                    pixY = event.y
                }
                MotionEvent.ACTION_POINTER_UP -> if (!moved) {
                    if (event.pointerCount == 2) {
                        onTwoFingerDoubleTap()
                        return true
                    }
                }
                MotionEvent.ACTION_MOVE -> if (abs(event.x - pixX) > threshold || abs(
                        event.y - pixY
                    ) > threshold
                ) {
                    moved = true
                }
            }
            return false
        }

        protected abstract fun onTwoFingerDoubleTap()
    }

}




