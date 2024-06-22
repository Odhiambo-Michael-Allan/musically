package com.odesa.musicMatters.core.common.media

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MediaPermissionsManager {

    private val _readExternalStoragePermissionGranted = MutableStateFlow( false )
    val readExternalStoragePermissionGranted = _readExternalStoragePermissionGranted.asStateFlow()

    private val _readMediaAudioPermissionGranted = MutableStateFlow( false )
    val readMediaAudioPermissionGranted = _readMediaAudioPermissionGranted.asStateFlow()

    private val _postNotificationPermissionGranted = MutableStateFlow( false )
    val postNotificationPermissionGranted = _postNotificationPermissionGranted

    private val _hasAllRequiredPermissions = MutableStateFlow( false )
    val hasAllRequiredPermissions: StateFlow<Boolean> = _hasAllRequiredPermissions.asStateFlow()

    fun readExternalStoragePermissionGranted( isGranted: Boolean, context: Context ) {
        _readExternalStoragePermissionGranted.value = isGranted
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions( context )
    }

    fun postNotificationPermissionGranted( isGranted: Boolean, context: Context ) {
        _postNotificationPermissionGranted.value = isGranted
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions( context )
    }

    fun readMediaAudioPermissionGranted( isGranted: Boolean, context: Context ) {
        _readMediaAudioPermissionGranted.value = isGranted
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions( context )
    }

    fun checkForPermissions( context: Context ) {
        _readExternalStoragePermissionGranted.value = hasReadExternalStoragePermission( context )
        _readMediaAudioPermissionGranted.value = hasReadMediaAudioPermission( context )
        _postNotificationPermissionGranted.value = hasPostNotificationPermissionGranted( context )
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions( context )
    }

    private fun hasReadExternalStoragePermission(
        context: Context
    ) = hasPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE )

    private fun hasReadMediaAudioPermission( context: Context ): Boolean {
        return if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
            hasPermission( context, Manifest.permission.READ_MEDIA_AUDIO )
        else false
    }

    private fun hasPostNotificationPermissionGranted( context: Context ): Boolean {
        return if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
            hasPermission( context, Manifest.permission.POST_NOTIFICATIONS )
        else false
    }

    private fun hasAllRequiredPermissions( context: Context ): Boolean {
        getRequiredPermissions().forEach {
            if ( !hasPermission( context, it ) ) {
                return false
            }
        }
        return true
    }

    private fun hasPermission( context: Context, permission: String ) =
        context.checkSelfPermission(
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun getRequiredPermissions(): List<String> {
        val requiredPermissions = mutableListOf<String>()
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            requiredPermissions.add( Manifest.permission.READ_MEDIA_AUDIO )
            requiredPermissions.add( Manifest.permission.POST_NOTIFICATIONS )
        } else {
            requiredPermissions.add( Manifest.permission.READ_EXTERNAL_STORAGE )
        }
        return requiredPermissions
    }

    companion object {

        @Volatile
        private var instance: MediaPermissionsManager? = null

        fun getInstance() = instance ?: synchronized( this ) {
            MediaPermissionsManager()
        }
    }
}
