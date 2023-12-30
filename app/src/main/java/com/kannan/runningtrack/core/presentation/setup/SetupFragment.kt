package com.kannan.runningtrack.core.presentation.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kannan.runningtrack.R
import com.kannan.runningtrack.databinding.FragmentSetupBinding
import com.kannan.runningtrack.utils.extensions.setOnDebounceClickListener
import com.kannan.runningtrack.utils.isAndroid33OrAbove
import com.kannan.runningtrack.utils.navigation.defaultNavOptsBuilder
import com.kannan.runningtrack.utils.permissions.NotificationPermissionHandler
import com.kannan.runningtrack.utils.permissions.PermissionHandler
import com.kannan.runningtrack.utils.permissions.PermissionResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private val viewModel : SetupViewModel by viewModels()

    private val timberTag = SetupFragment::class.java.simpleName

    private val notificationPermissionHandler : PermissionHandler by lazy {
        NotificationPermissionHandler(
            fragment = this,
            context = requireContext(),
            permissionResult = ::notificationPermissionResult
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSetupBinding.bind(view)

        requestNotificationPermission()

        binding.bindState(
            uiEvent = viewModel.uiEvent,
            uiAction = viewModel.accept
        )
    }

    private fun requestNotificationPermission(){
        if(isAndroid33OrAbove()) {
            notificationPermissionHandler.apply {
                viewLifecycleOwner.lifecycle.addObserver(this)
                onViewCreated()
                requestPermission()
            }
        }
    }

    private fun FragmentSetupBinding.bindState(
        uiEvent : SharedFlow<SetupUiEvent>,
        uiAction : ((SetupUiAction) -> Unit)
    ){
        uiEvent.onEach { event ->

            when(event){
                SetupUiEvent.NavigateToRunFragment -> navigateToRunFragment()
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindClick(uiAction)
    }

    private fun FragmentSetupBinding.bindClick(
        uiAction: ((SetupUiAction) -> Unit)
    ){
        tvContinue.setOnDebounceClickListener {
            uiAction.invoke(SetupUiAction.ContinueButtonClicked)
        }
    }

    private fun navigateToRunFragment(){
        findNavController().navigate(
            resId = R.id.runFragment,
            args = null,
            navOptions = defaultNavOptsBuilder().build()
        )
    }

    private fun notificationPermissionResult(permissionResult : PermissionResult){
        when(permissionResult){
            is PermissionResult.Error -> Timber.tag(timberTag).d("PermissionResult.Error ${permissionResult.error}")
            PermissionResult.PermissionGranted -> Timber.tag(timberTag).d("PermissionResult.PermissionGranted")
        }
    }
}