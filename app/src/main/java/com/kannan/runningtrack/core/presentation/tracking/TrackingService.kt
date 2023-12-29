package com.kannan.runningtrack.core.presentation.tracking

import android.content.Intent
import androidx.lifecycle.LifecycleService
import timber.log.Timber

class TrackingService : LifecycleService() {

    private val tag = TrackingService::class.java.simpleName
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when(it.action){
                TrackingServiceAction.START_OR_RESUME_SERVICE.name -> Timber.tag(tag).d("START_OR_RESUME_SERVICE")
                TrackingServiceAction.PAUSE_SERVICE.name -> Timber.tag(tag).d("PAUSE_SERVICE")
                TrackingServiceAction.RESUME_SERVICE.name -> Timber.tag(tag).d("RESUME_SERVICE")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}