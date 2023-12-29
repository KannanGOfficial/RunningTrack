package com.kannan.runningtrack.utils.permissions

sealed interface PermissionResult {
    data object PermissionGranted : PermissionResult
    data class Error(val error: String) : PermissionResult
}