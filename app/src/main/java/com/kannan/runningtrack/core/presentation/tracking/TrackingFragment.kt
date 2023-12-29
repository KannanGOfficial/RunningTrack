package com.kannan.runningtrack.core.presentation.tracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentTrackingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private lateinit var binding: FragmentTrackingBinding
    private var map: GoogleMap? = null

    private val viewModel : TrackingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTrackingBinding.bind(view)

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
        }

        binding.bindState(
            uiEvent = viewModel.uiEvent,
            uiAction = viewModel.accept
        )
    }

    private fun FragmentTrackingBinding.bindState(
        uiEvent : SharedFlow<TrackingUiEvent>,
        uiAction : ((TrackingUiAction) -> Unit)
    ){
        uiEvent.onEach { event ->
            when(event){
                is TrackingUiEvent.SendCommandToService -> sendCommandToService(event.action)
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindClick(uiAction)
    }

    private fun FragmentTrackingBinding.bindClick(
        uiAction: (TrackingUiAction) -> Unit
    ){
        btnToggleRun.setOnClickListener{
            uiAction.invoke(TrackingUiAction.ToggleRunButtonClicked)
        }
    }

    private fun sendCommandToService(action: TrackingServiceAction) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action.name
            requireContext().startService(it)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}