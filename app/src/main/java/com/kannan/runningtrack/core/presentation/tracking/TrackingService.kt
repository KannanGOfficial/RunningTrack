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
import com.kannan.runningtrack.utils.extensions.checkHasPermission
import com.kannan.runningtrack.utils.notifications.notifier.Notifier
import com.kannan.runningtrack.utils.notifications.notifier.TrackingNotifier
import com.kannan.runningtrack.utils.permissions.AppPermission
import timber.log.Timber

typealias polyLine = MutableList<LatLng>
typealias polyLines = MutableList<polyLine>

class TrackingService : LifecycleService() {

    var isFirstRun = true

    private val tag = TrackingService::class.java.simpleName

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        isTracking.observe(this){
            updateLocationTracking(it)
        }
    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<polyLines>()
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
//            it.last().add(latLng)
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
                    }else
                        Timber.tag(tag).d("Service is Resumed...")
                }
                TrackingServiceAction.PAUSE_SERVICE.name -> Timber.tag(tag).d("PAUSE_SERVICE")
                TrackingServiceAction.RESUME_SERVICE.name -> Timber.tag(tag).d("RESUME_SERVICE")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private val locationCallback = object :LocationCallback(){
        override fun onLocationResult(result: LocationResult) {
            if(isTracking.value!!){
                for(location in result.locations) {
                    addPolyLines(location)
                    Timber.tag(tag).d("New Location ${location.latitude} , ${location.longitude}")
                }
            }
        }
    }

    private fun startForegroundService(){
        addEmptyPolyLines()
        isTracking.postValue(true)
        startForeground(trackingNotifier.notificationId,trackingNotifier.notificationBuilder.build())
    }

    private fun updateLocationTracking(isTracking : Boolean){
        if(isTracking){
           if(checkHasPermission(AppPermission.COARSE_LOCATION)){
              val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5000L).apply {
                  setMinUpdateIntervalMillis(2000L)
                  setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                  setWaitForAccurateLocation(true)
              }.build()

               fusedLocationProviderClient.requestLocationUpdates(
                   request,
                   locationCallback,
                   Looper.getMainLooper()
               )
           }
        }
    }
}