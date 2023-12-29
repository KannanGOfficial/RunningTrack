package com.kannan.runningtrack.utils.permissions

import android.content.Context
import androidx.fragment.app.Fragment
import timber.log.Timber

class CoarseFineAndBackgroundLocationHandler(
    fragment: Fragment,
    permissionResult: (PermissionResult) -> Unit,
    private val context: Context
) : PermissionHandlerImpl(fragment, context, permissionResult) {


    override val permissionList: List<AppPermission> =
        if (isAndroid29OrAbove())
            listOf(
                AppPermission.BACKGROUND_LOCATION,
                AppPermission.FINE_LOCATION,
                AppPermission.COARSE_LOCATION
            )
        else
            listOf(
                AppPermission.FINE_LOCATION,
                AppPermission.COARSE_LOCATION
            )

    override fun showRationalDialog() {
        Timber.d("Rational Dialog is shown")
    }

    override fun showPermanentlyDeniedDialog() {
        Timber.d("Permanent Dialog is shown")
    }
}