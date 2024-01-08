package com.kannan.runningtrack.core.presentation.tracking

import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.kannan.runningtrack.utils.location.LocationTrackerFlow
import com.kannan.runningtrack.utils.location.LocationTrackerResult
import com.kannan.runningtrack.utils.location.RunningTrackLocationTracker
import com.kannan.runningtrack.utils.notifications.notifier.Notifier
import com.kannan.runningtrack.utils.notifications.notifier.TrackingNotifier
import com.kannan.runningtrack.utils.polylinecalculator.PloyLineCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService : LifecycleService() {

    private val tag = TrackingService::class.java.simpleName

    private val binder = TrackingServiceBinder()

    private var locationTrackerResult: ((LocationTrackerResult) -> Unit)? = null

    private val polyLineCalculator by lazy { PloyLineCalculator(){
    } }



    private val locationTracker: LocationTrackerFlow by lazy {
        RunningTrackLocationTracker(this) {
            locationTrackerResult?.invoke(it)
        }
    }


    val polyLines : PolyLines = mutableListOf(mutableListOf())

    val channels : Channel<PolyLine> = Channel()

    val polyLineFlow : Flow<PolyLines> = channelFlow{

        locationTracker.locationUpdatesFlow.onEach {
            val latLng = LatLng(it.latitude,it.longitude)
            polyLines.last().add(latLng)
            Timber.tag(tag).d("Locatoin Flow is collected inside the PolyLine Flow")
            launch {
                send(polyLines)
            }
//            emit(polyLines)
        }.launchIn(serviceScope)

        channels.consumeEach {
            polyLines.add(it)
            launch {
                send(polyLines)
            }
        }
    }

//    val polyLineFlow = polyLineCalculator.polyLineFlow

    val locationFlow = locationTracker.locationUpdatesFlow

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    inner class TrackingServiceBinder : Binder() {
        fun getBoundService(): TrackingService = this@TrackingService
    }

    fun setLocationTrackingResult(result: ((LocationTrackerResult) -> Unit)) {
        locationTrackerResult = result
    }

    override fun onCreate() {
        super.onCreate()
        /*        postInitialValues()
                isTracking.observe(this){
                    updateLocationTracking(it)
                }*/

        locationFlow
            .flatMapLatest {
                Timber.tag(tag)
                    .d("Location Updates are lat = ${it.latitude} long = ${it.longitude}")
                polyLineCalculator.calculateLocation(it)
            }.onEach {

            }


    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    private fun addEmptyPolyLines() = pathPoints.value?.let {
        it.add(mutableListOf())
        pathPoints.postValue(it)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPolyLines(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        pathPoints.value?.let {
            it.last().add(latLng)
            pathPoints.postValue(it)
        }
    }

    private val trackingNotifier: Notifier by lazy { TrackingNotifier(this) }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                TrackingServiceAction.START_OR_RESUME_SERVICE.name -> {
                    /*if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else {
                        Timber.tag(tag).d("Service is Resumed...")
                        startForegroundService()
                    }*/
                    startForegroundService()
                }

                TrackingServiceAction.PAUSE_SERVICE.name -> {
                    Timber.tag(tag).d("PAUSE_SERVICE")
                    pauseService()
                }

                TrackingServiceAction.STOP_SERVICE.name -> Timber.tag(tag).d("RESUME_SERVICE")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun pauseService() {
//        isTracking.postValue(false)
        locationTracker.stopLocationTracking()
    }

    private fun startForegroundService() {
//        addEmptyPolyLines()
//        isTracking.postValue(true)

        serviceScope.launch {
//            polyLineCalculator.addEmptyPolyLine()
            channels.send(mutableListOf())
        }
        locationTracker.startLocationTracking()
        startForeground(
            trackingNotifier.notificationId,
            trackingNotifier.notificationBuilder.build()
        )
    }

    /*    private fun updateLocationTracking(isTracking : Boolean) : Flow<Location> = callbackFlow{
            if(isTracking){
                Timber.tag(tag).d("Location Tracking is started..")
                locationTracker.startLocationTracking()
                *//*locationTracker.getLocationUpdates()
                .catch { e -> }
                .onEach {location ->
                    addPolyLines(location)
                    Timber.tag(tag).d("updateLocationTracking inside Flow called...")
                }.launchIn(serviceScope)*//*
        }
    }*/

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}