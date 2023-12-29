package com.kannan.runningtrack.utils.navigation

import androidx.navigation.NavOptions
import com.kannan.runningtrack.R

fun defaultNavOptsBuilder(): NavOptions.Builder {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_open_enter)
        .setExitAnim(R.anim.fragment_open_exit)
        .setPopEnterAnim(R.anim.fragment_close_enter)
        .setPopExitAnim(R.anim.fragment_close_exit)
}