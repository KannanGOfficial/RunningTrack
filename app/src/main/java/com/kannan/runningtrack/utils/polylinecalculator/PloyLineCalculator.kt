
package com.kannan.runningtrack.utils.polylinecalculator

import android.location.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class PloyLineCalculator {

    private lateinit var polyLineCallBack: ((Location) -> Unit)

    val polyLineFlow: Flow<Location> = callbackFlow {

        polyLineCallBack = {
            launch {
                send(it)
            }
        }
        awaitClose()
    }

    fun calculatePolyLine(locations: Location) {
        polyLineCallBack.invoke(locations)
    }

}