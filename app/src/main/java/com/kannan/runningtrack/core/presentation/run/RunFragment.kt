package com.kannan.runningtrack.core.presentation.run

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentRunBinding
import com.kannan.runningtrack.utils.navigation.defaultNavOptsBuilder
import com.kannan.runningtrack.utils.permissions.LocationPermissionHandler
import com.kannan.runningtrack.utils.permissions.PermissionHandler
import com.kannan.runningtrack.utils.permissions.PermissionResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val viewModel: RunViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRunBinding.bind(view)

        requestLocationPermission()

        binding.bindState(
            uiEvent = viewModel.uiEvent,
            uiAction = viewModel.accept
        )
    }

    private fun FragmentRunBinding.bindState(
        uiEvent : SharedFlow<RunUiEvent>,
        uiAction : ((RunUiAction) -> Unit)
    ){
        uiEvent.onEach { event ->
            when(event){
                RunUiEvent.NavigateToTrackingFragment -> navigateToTrackingFragment()
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindClick(uiAction)

    }

    private fun FragmentRunBinding.bindClick(uiAction: (RunUiAction) -> Unit){
        fab.setOnClickListener{
            uiAction.invoke(RunUiAction.AddRunButtonClicked)
        }
    }
    private fun navigateToTrackingFragment(){
        findNavController().navigate(
            resId = R.id.trackingFragment,
            args = null,
            navOptions = defaultNavOptsBuilder().build()
        )
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