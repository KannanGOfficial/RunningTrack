package com.kannan.runningtrack.core.presentation.run

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentRunBinding
import com.kannan.runningtrack.utils.permissions.LocationPermissionHandler
import com.kannan.runningtrack.utils.permissions.PermissionHandler
import com.kannan.runningtrack.utils.permissions.PermissionResult
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {

    private val locationPermissionHandler: PermissionHandler by lazy {
        LocationPermissionHandler(
            fragment = this,
            context = requireContext(),
            permissionResult = ::locationPermissionResult
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRunBinding.bind(view)
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        viewLifecycleOwner.lifecycle.addObserver(locationPermissionHandler)
        locationPermissionHandler.onViewCreated()
        locationPermissionHandler.requestPermission()
    }

    private fun locationPermissionResult(permissionResult: PermissionResult) {
        when (permissionResult) {
            is PermissionResult.Error -> Timber.d("LocationPermission : Error ${permissionResult.error}")
            PermissionResult.PermissionGranted -> Timber.d("LocationPermission : Successfully Granted...")
        }
    }
}