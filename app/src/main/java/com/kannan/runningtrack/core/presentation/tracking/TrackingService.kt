package com.kannan.runningtrack.core.presentation.tracking

import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.kannan.runningtrack.utils.extensions.checkHasLocationPermission
import com.kannan.runningtrack.utils.location.LocationTracker
import com.kannan.runningtrack.utils.location.LocationTrackerResult
import com.kannan.runningtrack.utils.location.RunningTrackLocationTracker
import com.kannan.runningtrack.utils.notifications.notifier.Notifier
import com.kannan.runningtrack.utils.notifications.notifier.TrackingNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService : LifecycleService() {

    private var isFirstRun = true

    private val tag = TrackingService::class.java.simpleName

    private val locationTracker : LocationTracker by lazy { RunningTrackLocationTracker(this, ::locationTrackingResult) }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        isTracking.observe(this){
            updateLocationTracking(it)
        }
    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<PolyLines>()
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    private fun addEmptyPolyLines() = pathPoints.value?.let {
        it.add(mutableListOf())
        pathPoints.postValue(it)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addPolyLines(location : Location) {
        val latLng = LatLng(location.latitude,location.longitude)
        pathPoints.value?.let {
            it.last().add(latLng)
            pathPoints.postValue(it)
        }
    }

    private val trackingNotifier : Notifier by lazy { TrackingNotifier(this) }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){
                TrackingServiceAction.START_OR_RESUME_SERVICE.name -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else {
                        Timber.tag(tag).d("Service is Resumed...")
                        startForegroundService()
                    }
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

    private fun pauseService(){
        isTracking.postValue(false)
    }

    private fun startForegroundService(){
        addEmptyPolyLines()
        isTracking.postValue(true)
        startForeground(trackingNotifier.notificationId,trackingNotifier.notificationBuilder.build())
    }

    private fun updateLocationTracking(isTracking : Boolean){
        if(isTracking){
            locationTracker.startLocationTracking()
            /*locationTracker.getLocationUpdates()
                .catch { e -> }
                .onEach {location ->
                    addPolyLines(location)
                    Timber.tag(tag).d("updateLocationTracking inside Flow called...")
                }.launchIn(serviceScope)*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
    private fun locationTrackingResult(locationTrackerResult: LocationTrackerResult){
        when(locationTrackerResult){
            is LocationTrackerResult.Error -> Timber.tag(tag).d(locationTrackerResult.error)
            is LocationTrackerResult.Success -> {
                if(isTracking.value!!){
                    for(location in locationTrackerResult.result.locations) {
                        addPolyLines(location)
                        Timber.tag(tag).d("New Location ${location.latitude} , ${location.longitude}")
                    }
                }
            }
        }
    }
}