package com.kannan.runningtrack.utils.permissions

import androidx.lifecycle.DefaultLifecycleObserver

interface PermissionHandler : DefaultLifecycleObserver {
    fun onViewCreated()

    fun requestPermission()
}