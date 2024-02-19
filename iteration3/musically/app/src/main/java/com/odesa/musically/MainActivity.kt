package com.odesa.musically

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.odesa.musically.data.AppContainer
import com.odesa.musically.services.PermissionsManager
import com.odesa.musically.ui.MusicallyApp
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        Timber.plant( Timber.DebugTree() )
        appContainer = ( application as MusicallyApplication ).container
        val mainActivityViewModel: MainActivityViewModel by viewModels {
            MainActivityViewModel.MainActivityViewModelFactory(
                application,
                appContainer.musicServiceConnection
            )
        }

        // Allow app to draw behind system bar decorations (e.g.: navbar)
        WindowCompat.setDecorFitsSystemWindows( window, false )
        PermissionsManager.requestPermissions( this )

        setContent {
            MusicallyApp(
                settingsRepository = appContainer.settingsRepository,
                musicServiceConnection = appContainer.musicServiceConnection,
                mainActivityViewModel = mainActivityViewModel
            )
        }
    }
}
