package com.kannan.runningtrack.core.presentation.run

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.runningtrack.core.domain.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(private val runRepository: RunRepository) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<RunUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept = ::onUiAction

    private fun onUiAction(action : RunUiAction){
        when(action){
            RunUiAction.AddRunButtonClicked -> sendEvent(RunUiEvent.NavigateToTrackingFragment)
        }
    }

    private fun sendEvent(event: RunUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }
}