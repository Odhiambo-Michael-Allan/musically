package com.odesa.musicMatters.services

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.odesa.musicMatters.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


object PermissionsManager {

    private val _mediaPermissionGranted = MutableStateFlow( false )
    val mediaPermissionGranted = _mediaPermissionGranted.asStateFlow()

    fun requestPermissions( activity: MainActivity ) {
        val permissionState = getPermissionState( activity )
        if ( permissionState.hasAllPermissions ) {
            _mediaPermissionGranted.value = true
            return
        }
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if ( permissions.count { it.value } > 0 )
                _mediaPermissionGranted.value = true
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

data class PermissionState(
    val requiredPermissions: List<String>,
    val grantedPermissions: List<String>,
    val deniedPermissions: List<String>
) {
    val hasAllPermissions = deniedPermissions.isEmpty()
}

