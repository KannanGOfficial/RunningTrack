package com.kannan.runningtrack.core.presentation.run

import androidx.lifecycle.ViewModel
import com.kannan.runningtrack.core.domain.repository.RunRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(private val runRepository: RunRepository) : ViewModel() {
}