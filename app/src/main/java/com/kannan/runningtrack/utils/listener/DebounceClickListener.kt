package com.kannan.runningtrack.utils.listener

import android.os.SystemClock
import android.view.View
import java.util.WeakHashMap
import kotlin.math.abs

abstract class DebounceClickListener : View.OnClickListener {

    private val lastClickMap: MutableMap<View, Long>
    private val minIntervalMillis: Long = 500L

    init {
        this.lastClickMap = WeakHashMap()
    }

    /**
     * Implement this method instead of onClick
     * @param view The vie
     */
    abstract fun onDebounceClick(view: View)

    override fun onClick(view: View) {
        val previousClickTimestamp = lastClickMap[view]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[view] = currentTimestamp
        if(previousClickTimestamp == null || abs(currentTimestamp - previousClickTimestamp) > minIntervalMillis) {
            onDebounceClick(view)
        }
    }
}