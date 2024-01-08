package com.kannan.runningtrack.utils.polylinecalculator

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.kannan.runningtrack.core.presentation.tracking.PolyLines
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class PloyLineCalculator ( val polyLineCallBack1 : ((PolyLines) -> Unit)) {

    val polyLines : PolyLines = mutableListOf(mutableListOf())

    private lateinit var polyLineCallBack: ((PolyLines) -> Unit)

    private lateinit var polyLineInterfaceCallBack : PolyLineInterface

    val polyLineFlow: Flow<PolyLines> = callbackFlow {

        polyLineInterfaceCallBack = object : PolyLineInterface{
            override fun polyLineCallBack(polyLines: PolyLines) {
                launch {
                    send(polyLines)
                }
            }
        }
        polyLineCallBack = {
            launch {
                send(it)
            }
        }
        awaitClose()
    }

    fun calculateLocation(locations: Location) : Flow<PolyLines> = flow{
        val latLng = LatLng(locations.latitude,locations.longitude)
        polyLines.last().add(latLng)
//        polyLineCallBack1.invoke(polyLines)
        emit(polyLines)
    }

    fun addEmptyPolyLine1() : Flow<PolyLines> = flow{
        polyLines.add(mutableListOf())
//        polyLineCallBack1.invoke(polyLines)
        emit(polyLines)
    }

    suspend fun calculatePolyLine(locations: Location) {
        val polyLines = polyLineFlow.lastOrNull()
        val latLng = LatLng(locations.latitude, locations.longitude)
        polyLines?.let {
            polyLines.last().add(latLng)
            polyLineCallBack.invoke(polyLines)
            polyLineInterfaceCallBack.polyLineCallBack(polyLines)
        }
    }

    suspend fun addEmptyPolyLine() {
        val polyLines = polyLineFlow.lastOrNull()
        polyLines?.let {
            polyLines.add(mutableListOf())
            polyLineCallBack.invoke(polyLines)
            polyLineInterfaceCallBack.polyLineCallBack(polyLines)
        } ?:/* polyLineCallBack.invoke(mutableListOf(mutableListOf()))*/ polyLineInterfaceCallBack.polyLineCallBack(
            mutableListOf()
        )
    }

    fun postInitialValues() {
//        polyLineCallBack.invoke(mutableListOf())
        polyLineInterfaceCallBack.polyLineCallBack(mutableListOf())
    }

}

interface PolyLineInterface {
    fun polyLineCallBack(polyLines: PolyLines)
}