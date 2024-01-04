package com.kannan.runningtrack.core.presentation.tracking

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.runningtrack.core.domain.repository.RunRepository
import com.kannan.runningtrack.utils.location.LocationTrackerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(private val runRepository: RunRepository) : ViewModel() {

    private val timberTag =  TrackingViewModel::class.java.simpleName

    private val _uiEvent : MutableSharedFlow<TrackingUiEvent> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept = ::onUiAction
    private fun onUiAction(event : TrackingUiAction){
        when(event){
            TrackingUiAction.ToggleRunButtonClicked -> sendUiEvent(TrackingUiEvent.SendCommandToService(TrackingServiceAction.START_OR_RESUME_SERVICE))
        }
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            val trackingServiceBinder = binder as TrackingService.TrackingServiceBinder
            trackingServiceBinder.getBoundService().setLocationTrackingResult(::locationTrackerResult)
        }
        override fun onServiceDisconnected(p0: ComponentName?) {}
    }

    private fun locationTrackerResult(locationTrackerResult : LocationTrackerResult){
        when(locationTrackerResult){
            is LocationTrackerResult.Error -> Timber.tag(timberTag).d(locationTrackerResult.error)
            is LocationTrackerResult.Success -> {
                if(TrackingService.isTracking.value!!){
                    for(location in locationTrackerResult.result.locations) {
                        Timber.tag(timberTag).d("New Location ${location.latitude} , ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event : TrackingUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }

}