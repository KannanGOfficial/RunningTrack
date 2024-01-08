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



    private val locationTracker: LocationTrackerFlow by lazy {
        RunningTrackLocationTracker(this) {
            locationTrackerResult?.invoke(it)
        }
    }


    private val polyLines : PolyLines = mutableListOf(mutableListOf())

    private val channels : Channel<PolyLine> = Channel()

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

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    inner class TrackingServiceBinder : Binder() {
        fun getBoundService(): TrackingService = this@TrackingService
    }

    private val trackingNotifier: Notifier by lazy { TrackingNotifier(this) }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                TrackingServiceAction.START_OR_RESUME_SERVICE.name -> {
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
        locationTracker.stopLocationTracking()
    }

    private fun startForegroundService() {
        serviceScope.launch {
            channels.send(mutableListOf())
        }
        locationTracker.startLocationTracking()
        startForeground(
            trackingNotifier.notificationId,
            trackingNotifier.notificationBuilder.build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}