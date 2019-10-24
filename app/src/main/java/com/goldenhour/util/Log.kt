@file:JvmName("Log")
@file:JvmMultifileClass

package com.goldenhour.util

import com.goldenhour.BuildConfig

/**
 * Custom log class
 */
private val PRINT = BuildConfig.DEBUG

fun info(tag: String, message: String) {
    if (PRINT) {
        android.util.Log.i(tag, message)
    }
}

fun debug(tag: String, message: String) {
    if (PRINT) {
        android.util.Log.d(tag, message)
    }
}

fun error(tag: String, message: String) {
    if (PRINT) {
        android.util.Log.e(tag, message)
    }
}

fun verbose(tag: String, message: String) {
    if (PRINT) {
        android.util.Log.v(tag, message)
    }
}

fun warn(tag: String, message: String) {
    if (PRINT) {
        android.util.Log.w(tag, message)
    }
}

