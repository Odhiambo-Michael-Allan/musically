package com.odesa.musically.services

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.odesa.musically.MainActivity


object PermissionsManager {

    var mediaPermissionGranted = false

    fun requestPermissions( activity: MainActivity ) {
        val permissionState = getPermissionState( activity )
        if ( permissionState.hasAllPermissions ) return
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if ( permissions.count { it.value } > 0 )
                mediaPermissionGranted = true
        }.launch( permissionState.deniedPermissions.toTypedArray() )
    }

    private fun getPermissionState( activity: MainActivity ): PermissionState {
        val requiredPermissions = getRequiredPermissions()
        val grantedPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()
        requiredPermissions.forEach {
            if ( activity.checkSelfPermission( it ) == PackageManager.PERMISSION_GRANTED )
                grantedPermissions.add( it )
            else
                deniedPermissions.add( it )
        }
        return PermissionState(
            requiredPermissions = requiredPermissions,
            grantedPermissions = grantedPermissions,
            deniedPermissions = deniedPermissions
        )
    }

    private fun getRequiredPermissions(): List<String> {
        val requiredPermissions = mutableListOf( Manifest.permission.READ_EXTERNAL_STORAGE )
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            requiredPermissions.add( Manifest.permission.READ_MEDIA_AUDIO )
            requiredPermissions.add( Manifest.permission.POST_NOTIFICATIONS )
        }
        return requiredPermissions
    }
}
enum class PermissionEvent {
    MEDIA_PERMISSION_GRANTED_EVENT
}

data class PermissionState(
    val requiredPermissions: List<String>,
    val grantedPermissions: List<String>,
    val deniedPermissions: List<String>
) {
    val hasAllPermissions = deniedPermissions.isEmpty()
}