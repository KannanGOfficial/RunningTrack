package com.kannan.runningtrack.core.presentation.tracking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentTrackingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTrackingBinding.bind(view)
    }
}