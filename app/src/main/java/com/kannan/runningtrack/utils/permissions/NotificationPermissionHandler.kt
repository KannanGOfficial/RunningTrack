package com.kannan.runningtrack.utils.permissions

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import timber.log.Timber

class NotificationPermissionHandler(
    fragment: Fragment,
    context: Context,
    permissionResult: (PermissionResult) -> Unit
) : PermissionHandlerImpl(
    fragment = fragment,
    context = context,
    permissionResult = permissionResult
) {

    override val timberTag: String = NotificationPermissionHandler::class.java.simpleName

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override val permissionList: List<AppPermission> =
        listOf(AppPermission.NOTIFICATION_PERMISSION)

    override fun showRationalDialog() {
        Timber.tag(timberTag).d("showRationalDialog")
    }

    override fun showPermanentlyDeniedDialog() {
        Timber.tag(timberTag).d("showPermanentlyDeniedDialog")
    }
}