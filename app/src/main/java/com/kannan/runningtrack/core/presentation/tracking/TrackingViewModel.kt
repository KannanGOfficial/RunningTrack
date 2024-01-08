package com.kannan.runningtrack.core.presentation.tracking

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.runningtrack.R
import com.kannan.runningtrack.core.domain.repository.RunRepository
import com.kannan.runningtrack.utils.uitext.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(private val runRepository: RunRepository) :
    ViewModel() {

    private val timberTag = TrackingViewModel::class.java.simpleName

    private val _uiState: MutableStateFlow<TrackingUiState> = MutableStateFlow(TrackingUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = TrackingUiState(),
    )


    private val _uiEvent: MutableSharedFlow<TrackingUiEvent> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept = ::onUiAction
    private fun onUiAction(event: TrackingUiAction) {
        when (event) {
            TrackingUiAction.ToggleRunButtonClicked -> toggleRun()
        }
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            val trackingServiceBinder = binder as TrackingService.TrackingServiceBinder
            trackingServiceBinder.getBoundService()
                .polyLineFlow.onEach {
                    Timber.tag(timberTag).d("!!!Locations are : $it")
                }.launchIn(viewModelScope)

        }

        override fun onServiceDisconnected(p0: ComponentName?) {}
    }

/*
    private fun locationTrackerResult(locationTrackerResult: LocationTrackerResult) {
        when (locationTrackerResult) {
            is LocationTrackerResult.Error -> Timber.tag(timberTag).d(locationTrackerResult.error)
            is LocationTrackerResult.Success -> {
                if (TrackingService.isTracking.value!!) {
                    for (location in locationTrackerResult.result.locations) {
                        Timber.tag(timberTag)
                            .d("New Location ${location.latitude} , ${location.longitude}")
                    }
                }
            }
        }
    }
*/

    private fun sendUiEvent(event: TrackingUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }

    private fun updateTrackingUiState(trackingState : TrackingState) = _uiState.update {
        it.copy(
            trackingState = trackingState
        )
    }

    private fun updateToggleRunButtonText(text : UiText) = _uiState.update {
        it.copy(
            toggleRunButtonText = text
        )
    }

    private fun updateTrackingServiceAction(trackingServiceAction: TrackingServiceAction) = _uiState.update {
        it.copy(
            trackingServiceAction = trackingServiceAction
        )
    }

    private fun toggleRun(){
        when(_uiState.value.trackingState){
            TrackingState.TRACKING -> {
                updateToggleRunButtonText(UiText.StringResource(R.string.start))
                updateTrackingUiState(TrackingState.NOT_TRACKING)
                updateTrackingServiceAction(TrackingServiceAction.PAUSE_SERVICE)
            }
            TrackingState.NOT_TRACKING -> {
                updateToggleRunButtonText(UiText.StringResource(R.string.stop))
                updateTrackingUiState(TrackingState.TRACKING)
                updateTrackingServiceAction(TrackingServiceAction.START_OR_RESUME_SERVICE)

            }
        }
    }

}