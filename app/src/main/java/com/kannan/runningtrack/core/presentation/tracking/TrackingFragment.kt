package com.kannan.runningtrack.core.presentation.tracking

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentTrackingBinding
import com.kannan.runningtrack.utils.extensions.makeGone
import com.kannan.runningtrack.utils.extensions.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private lateinit var binding: FragmentTrackingBinding
    private var map: GoogleMap? = null

    private var isTracking : Boolean = false
    private var pathPoints =  mutableListOf<PolyLine>()

    private val timberTag = TrackingFragment::class.java.simpleName

    private val viewModel : TrackingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTrackingBinding.bind(view)

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
            addAllPolyLine()
        }

        subscribeObservers()

        binding.bindState(
            uiEvent = viewModel.uiEvent,
            uiAction = viewModel.accept
        )
    }

    fun subscribeObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner){
           updateTracking(it)
        }

        TrackingService.pathPoints.observe(viewLifecycleOwner){
            pathPoints = it
            addLatestPolyLine()
            moveCameraToUser()
        }
    }

    private fun updateTracking(isTracking : Boolean){
        this.isTracking = isTracking
        if(isTracking){
            binding.btnToggleRun.text = "Stop"
            binding.btnFinishRun.makeGone()
        }else{
            binding.btnToggleRun.text ="Start"
            binding.btnFinishRun.makeVisible()
        }
    }

    private fun addLatestPolyLine(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLng = pathPoints.last().last()
            val polyLineOptions = PolylineOptions()
                .color(Color.RED)
                .add(preLastLatLng)
                .add(lastLng)

            Timber.tag(timberTag).d("addLatestPolyLine $preLastLatLng , $lastLng")
            map?.addPolyline(polyLineOptions)
        }
    }

    private fun addAllPolyLine(){
        for(polyLine in pathPoints){
            val polyLineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .addAll(polyLine)
            map?.addPolyline(polyLineOptions)
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    100f
                )
            )
        }
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
//            uiAction.invoke(TrackingUiAction.ToggleRunButtonClicked)
            toggleRun()
        }
    }

    private fun sendCommandToService(action: TrackingServiceAction) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action.name
            requireContext().startService(it)
        }
    }

    private fun toggleRun(){
        if(isTracking)
            sendCommandToService(TrackingServiceAction.PAUSE_SERVICE)
        else
            sendCommandToService(TrackingServiceAction.START_OR_RESUME_SERVICE)
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