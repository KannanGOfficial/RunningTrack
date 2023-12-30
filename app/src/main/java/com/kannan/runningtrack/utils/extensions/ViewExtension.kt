package com.kannan.runningtrack.utils.extensions

import android.view.View
import com.kannan.runningtrack.utils.listener.DebounceClickListener

fun View.setOnDebounceClickListener(onClick: (View) -> Unit) {
    val debounceClickListener = object : DebounceClickListener() {
        override fun onDebounceClick(view: View) {
            onClick(view)
        }
    }
    setOnClickListener(debounceClickListener)
}
