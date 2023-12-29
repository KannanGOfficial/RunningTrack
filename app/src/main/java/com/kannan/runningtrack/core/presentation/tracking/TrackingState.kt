package com.kannan.runningtrack.core.presentation.tracking

enum class TrackingServiceAction{
    START_OR_RESUME_SERVICE,
    PAUSE_SERVICE,
    RESUME_SERVICE
}

sealed interface TrackingUiAction{
    data object ToggleRunButtonClicked : TrackingUiAction
}

sealed interface TrackingUiEvent{
    data class SendCommandToService(val action : TrackingServiceAction) : TrackingUiEvent
}