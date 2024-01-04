package com.kannan.runningtrack.core.presentation.tracking

import com.kannan.runningtrack.utils.uitext.UiText

enum class TrackingServiceAction{
    START_OR_RESUME_SERVICE,
    PAUSE_SERVICE,
    STOP_SERVICE
}

sealed interface TrackingUiAction{
    data object ToggleRunButtonClicked : TrackingUiAction
}

sealed interface TrackingUiEvent{
    data class SendCommandToService(val action : TrackingServiceAction) : TrackingUiEvent
}
data class TrackingUiState(
    val trackingState : TrackingState = TrackingState.NOT_TRACKING,
    val trackingServiceAction : TrackingServiceAction = TrackingServiceAction.STOP_SERVICE,
    val toggleRunButtonText : UiText? = null
)

enum class TrackingState{
    TRACKING,
    NOT_TRACKING
}