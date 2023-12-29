package com.kannan.runningtrack.utils

import android.os.Build

fun isAndroid29OrAbove() =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q