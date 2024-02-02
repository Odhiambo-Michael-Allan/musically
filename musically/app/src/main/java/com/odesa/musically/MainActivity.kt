package com.odesa.musically

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import com.odesa.musically.services.PermissionsManager
import com.odesa.musically.ui.MusicallyApp

class MainActivity : ComponentActivity() {
    @RequiresApi( Build.VERSION_CODES.O )
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        val appContainer = ( application as MusicallyApplication ).container

        // Allow app to draw behind system bar decorations (e.g.: navbar)
        WindowCompat.setDecorFitsSystemWindows( window, false )
        PermissionsManager.requestPermissions( this )

        setContent {
            MusicallyApp(
                settingsRepository = appContainer.settingsRepository,
                songsRepository = appContainer.songsRepository
            )
        }
    }
}
