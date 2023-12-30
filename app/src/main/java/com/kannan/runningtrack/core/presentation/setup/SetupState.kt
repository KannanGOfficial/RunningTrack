package com.kannan.runningtrack.core.presentation.setup

sealed interface SetupUiAction{
    data object ContinueButtonClicked : SetupUiAction
}

sealed interface SetupUiEvent {
    data object NavigateToRunFragment : SetupUiEvent
}