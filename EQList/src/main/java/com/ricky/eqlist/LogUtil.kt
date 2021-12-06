package com.ricky.eqlist

import android.util.Log

/**
 *
 * @author Ricky Hal
 * @date 2021/11/29
 */
object LogUtil {
    var isDebug: Boolean = false
    private const val TAG = "EQList"

    fun d(msg: String) {
        if (isDebug) Log.d(TAG, msg)
    }

    fun e(t: Throwable) {
        if (isDebug) Log.e(TAG, t.stackTraceToString())
    }

    fun e(exception: Exception) {
        if (isDebug) Log.e(TAG, exception.stackTraceToString())
    }

    fun e(msg: String) {
        if (isDebug) Log.e(TAG, msg)
    }

    fun w(msg: String) {
        if (isDebug) Log.w(TAG, msg)
    }
}