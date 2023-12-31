package com.kannan.runningtrack.utils.permissions

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.kannan.runningtrack.utils.extensions.checkHasPermission

abstract class PermissionHandlerImpl(
    private val fragment: Fragment,
    private val context : Context,
    private val permissionResult: (PermissionResult) -> Unit
) : PermissionHandler {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(){
        permissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ::onPermissionResult
        )
    }

    override fun requestPermission() {
        try {
            if(checkAlreadyHasPermission())
                permissionResult.invoke(PermissionResult.PermissionGranted)
            else
                permissionLauncher.launch(permissionList.map { it.value }.toTypedArray())
        }catch (e : Exception){
            permissionResult.invoke(PermissionResult.Error(e.toString()))
        }
    }

    abstract val permissionList: List<AppPermission>

    abstract val timberTag : String

    abstract fun showRationalDialog()

    abstract fun showPermanentlyDeniedDialog()

    private fun onPermissionResult(result :  Map<String, Boolean>) {

        val isPermissionGranted = result.entries.all { it.value }

        val needToShowRationalDialog = permissionList.any { fragment.shouldShowRequestPermissionRationale(it.value)}
        when {
            isPermissionGranted -> permissionResult.invoke(PermissionResult.PermissionGranted)

            needToShowRationalDialog -> showRationalDialog()

            else -> showPermanentlyDeniedDialog()
        }
    }

    private fun checkAlreadyHasPermission () =
        permissionList.all { permission ->
            context.checkHasPermission(permission)
        }

}