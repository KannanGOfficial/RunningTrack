package com.kannan.runningtrack.core.presentation.tracking

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentTrackingBinding
import com.kannan.runningtrack.utils.extensions.makeGone
import com.kannan.runningtrack.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private lateinit var binding: FragmentTrackingBinding
    private var map: GoogleMap? = null

    private val timberTag = TrackingFragment::class.java.simpleName

    private val viewModel : TrackingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTrackingBinding.bind(view)

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
//            addAllPolyLine()
        }


        binding.bindState(
            uiEvent = viewModel.uiEvent,
            uiAction = viewModel.accept,
            uiState = viewModel.uiState
        )
    }

    private fun FragmentTrackingBinding.bindState(
        uiEvent : SharedFlow<TrackingUiEvent>,
        uiAction : ((TrackingUiAction) -> Unit),
        uiState : StateFlow<TrackingUiState>,
    ){
        uiEvent.onEach { event ->
            when(event){
                is TrackingUiEvent.SendCommandToService -> sendCommandToService(event.action)
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindToggleRun(
            uiState
        )

        bindClick(uiAction)
    }

    private fun FragmentTrackingBinding.bindToggleRun(
        uiState: StateFlow<TrackingUiState>
    ){
        val trackingState = uiState.map { it.trackingState }.distinctUntilChanged()
        val trackingButtonText = uiState.mapNotNull { it.toggleRunButtonText }.distinctUntilChanged()
        val trackingServiceAction = uiState.map { it.trackingServiceAction }.distinctUntilChanged()
        val cameraPosition = uiState.mapNotNull { it.cameraPosition }.distinctUntilChanged()
        val lastAndPreLastLatLng = uiState.mapNotNull { it.lastAndPreLastLatLng }.distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                launch {
                    trackingState.collectLatest {trackingState ->
                        when(trackingState){
                            TrackingState.TRACKING -> {
                                btnFinishRun.makeVisible()
                            }
                            TrackingState.NOT_TRACKING -> {
                                btnFinishRun.makeGone()
                            }
                        }
                    }
                }

                launch {
                    trackingButtonText.collectLatest { text ->
                        btnToggleRun.text = text.asString(requireContext())
                    }
                }

                launch {
                    trackingServiceAction.collectLatest { action ->
                        sendCommandToService(action)
                    }
                }
                launch {
                    cameraPosition.collectLatest {latLng ->
                        map?.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                latLng,
                                100f
                            )
                        )
                    }
                }

                launch {
                    lastAndPreLastLatLng.collectLatest {
                        val polyLineOptions = PolylineOptions()
                            .color(Color.RED)
                            .add(it.preLastLatLng)
                            .add(it.lastLatLng)
                        map?.addPolyline(polyLineOptions)
                    }
                }
            }
        }
    }

    private fun FragmentTrackingBinding.bindClick(
        uiAction: (TrackingUiAction) -> Unit
    ){
        btnToggleRun.setOnClickListener{
            uiAction.invoke(TrackingUiAction.ToggleRunButtonClicked)
//            toggleRun()
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
        Intent(requireContext(), TrackingService::class.java).also { intent ->
            requireContext().bindService(intent, viewModel.serviceConnection, Context.BIND_AUTO_CREATE)
        }
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
        requireContext().unbindService(viewModel.serviceConnection)
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