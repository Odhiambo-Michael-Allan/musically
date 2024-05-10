package com.odesa.musicMatters.services

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import com.odesa.musicMatters.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber


object PermissionsManager {

    private var activity: MainActivity? = null

    private val _readExternalStoragePermissionGranted = MutableStateFlow( hasReadExternalStoragePermission() )
    val readExternalStoragePermissionGranted = _readExternalStoragePermissionGranted.asStateFlow()

    private val _readMediaAudioPermissionGranted = MutableStateFlow( hasReadMediaAudioPermission() )
    val readMediaAudioPermissionGranted = _readMediaAudioPermissionGranted.asStateFlow()

    private val _postNotificationPermissionGranted = MutableStateFlow( hasPostNotificationPermissionGranted() )
    val postNotificationPermissionGranted = _postNotificationPermissionGranted

    private val _hasAllRequiredPermissions = MutableStateFlow( hasAllRequiredPermissions() )
    val hasAllRequiredPermissions: StateFlow<Boolean> = _hasAllRequiredPermissions.asStateFlow()


    fun readExternalStoragePermissionGranted( isGranted: Boolean ) {
        _readExternalStoragePermissionGranted.value = isGranted
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions()
    }

    fun postNotificationPermissionGranted( isGranted: Boolean ) {
        _postNotificationPermissionGranted.value = isGranted
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions()
    }

    fun readMediaAudioPermissionGranted( isGranted: Boolean ) {
        _readMediaAudioPermissionGranted.value = isGranted
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions()
    }

    fun setActivity( activity: MainActivity) {
        this.activity = activity
        _readExternalStoragePermissionGranted.value = hasReadExternalStoragePermission()
        _readMediaAudioPermissionGranted.value = hasReadMediaAudioPermission()
        _postNotificationPermissionGranted.value = hasPostNotificationPermissionGranted()
        _hasAllRequiredPermissions.value = hasAllRequiredPermissions()
    }

    private fun hasReadExternalStoragePermission() = hasPermission( Manifest.permission.READ_EXTERNAL_STORAGE )


    private fun hasReadMediaAudioPermission(): Boolean {
        return if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
            hasPermission( Manifest.permission.READ_MEDIA_AUDIO )
        else false
    }

    private fun hasPostNotificationPermissionGranted(): Boolean {
        return if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU )
            hasPermission( Manifest.permission.POST_NOTIFICATIONS )
        else false
    }

    private fun hasSetRingtoneNotificationGranted() = hasPermission( Manifest.permission.WRITE_SETTINGS )

    private fun hasAllRequiredPermissions(): Boolean {
        getRequiredPermissions().forEach {
            if ( !hasPermission( it ) ) {
                Timber.tag( TAG ).d( "DOES NOT HAVE ALL REQUIRED PERMISSIONS.." )
                return false
            }
        }
        Timber.tag( TAG ).d( "HAS ALL REQUIRED PERMISSIONS.." )
        return true
    }

    private fun hasPermission( permission: String ) = this.activity?.let {
        it.checkSelfPermission(
            permission
        ) == PackageManager.PERMISSION_GRANTED
    } ?: false

    private fun getRequiredPermissions(): List<String> {
        val requiredPermissions = mutableListOf( Manifest.permission.READ_EXTERNAL_STORAGE )
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
            requiredPermissions.add( Manifest.permission.READ_MEDIA_AUDIO )
            requiredPermissions.add( Manifest.permission.POST_NOTIFICATIONS )
        }
        Timber.tag( TAG ).d( "REQUIRED PERMISSIONS SIZE: ${requiredPermissions.size}" )
        return requiredPermissions
    }
}

const val TAG = "PERMISSIONS MANAGER"


