package com.kannan.runningtrack.core.presentation.run

sealed interface RunUiAction{
    data object AddRunButtonClicked : RunUiAction
}

sealed interface RunUiEvent{
    data object NavigateToTrackingFragment : RunUiEvent
}