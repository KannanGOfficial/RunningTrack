package com.kannan.runningtrack.core.presentation.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.runningtrack.core.domain.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(private val runRepository: RunRepository) : ViewModel() {

    private val _uiEvent : MutableSharedFlow<TrackingUiEvent> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept = ::onUiAction
    private fun onUiAction(event : TrackingUiAction){
        when(event){
            TrackingUiAction.ToggleRunButtonClicked -> sendUiEvent(TrackingUiEvent.SendCommandToService(TrackingServiceAction.START_OR_RESUME_SERVICE))
        }
    }

    private fun sendUiEvent(event : TrackingUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }

}