package com.kannan.runningtrack.utils.permissions

import android.content.Context
import androidx.fragment.app.Fragment
import timber.log.Timber

class LocationPermissionHandler(
    fragment: Fragment,
    permissionResult: (PermissionResult) -> Unit,
    private val context: Context
) : PermissionHandlerImpl(fragment, context, permissionResult) {

    override val timberTag: String =
        LocationPermissionHandler::class.java.simpleName

    override val permissionList: List<AppPermission> =
            listOf(
                AppPermission.COARSE_LOCATION,
                AppPermission.FINE_LOCATION
            )

    override fun showRationalDialog() {
        Timber.tag(timberTag).d("Rational Dialog is shown")
    }

    override fun showPermanentlyDeniedDialog() {
        Timber.tag(timberTag).d("Permanent Dialog is shown")
    }
}