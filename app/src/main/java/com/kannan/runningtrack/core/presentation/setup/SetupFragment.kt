package com.kannan.runningtrack.core.presentation.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentSetupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSetupBinding.bind(view)
    }
}