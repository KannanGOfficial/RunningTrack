package com.kannan.runningtrack.core.presentation.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentStatisticBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticFragment : Fragment(R.layout.fragment_statistic) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStatisticBinding.bind(view)
    }
}