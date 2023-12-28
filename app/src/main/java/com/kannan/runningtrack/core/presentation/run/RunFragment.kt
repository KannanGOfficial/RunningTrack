package com.kannan.runningtrack.core.presentation.run

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentRunBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRunBinding.bind(view)
    }
}