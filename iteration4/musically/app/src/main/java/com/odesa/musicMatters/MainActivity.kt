package com.odesa.musicMatters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.odesa.musicMatters.data.AppContainer
import com.odesa.musicMatters.services.PermissionsManager
import com.odesa.musicMatters.ui.MusicallyApp
import com.odesa.musicMatters.ui.nowPlaying.NowPlayingViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        Timber.plant( Timber.DebugTree() )
        appContainer = ( application as MusicMatters ).container
        val nowPlayingViewModel: NowPlayingViewModel by viewModels {
            NowPlayingViewModel.NowPlayingViewModelFactory(
                application,
                appContainer.musicServiceConnection,
                appContainer.settingsRepository,
                appContainer.playlistRepository
            )
        }

        // Allow app to draw behind system bar decorations (e.g.: navbar)
        WindowCompat.setDecorFitsSystemWindows( window, false )
        PermissionsManager.requestPermissions( this )

        setContent {
            MusicallyApp(
                settingsRepository = appContainer.settingsRepository,
                musicServiceConnection = appContainer.musicServiceConnection,
                playlistRepository = appContainer.playlistRepository,
                nowPlayingViewModel = nowPlayingViewModel
            )
        }
    }
}
