package com.kannan.runningtrack.core.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.runningtrack.core.domain.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(private val runRepository: RunRepository) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<SetupUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept = ::onUiAction

    private fun onUiAction(action : SetupUiAction){

        when(action){
            SetupUiAction.ContinueButtonClicked -> sendUiEvent(SetupUiEvent.NavigateToRunFragment)
        }
    }

    private fun sendUiEvent(event : SetupUiEvent)= viewModelScope.launch{
        _uiEvent.emit(event)
    }

}