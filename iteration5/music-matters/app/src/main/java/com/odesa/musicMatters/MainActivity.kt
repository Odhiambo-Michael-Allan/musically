package com.odesa.musicMatters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.odesa.musicMatters.data.AppContainer
import com.odesa.musicMatters.services.PermissionsManager
import com.odesa.musicMatters.ui.MusicallyApp
import com.odesa.musicMatters.ui.components.PermissionsScreen
import com.odesa.musicMatters.ui.nowPlaying.NowPlayingViewModel
import com.odesa.musicMatters.ui.theme.MusicMattersTheme
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
        PermissionsManager.setActivity( this )

        setContent {

            val allRequiredPermissionsHaveBeenGranted by PermissionsManager.hasAllRequiredPermissions.collectAsState()
            val postNotificationsPermissionGranted by PermissionsManager.postNotificationPermissionGranted.collectAsState()
            val readExternalStoragePermissionGranted by PermissionsManager.readExternalStoragePermissionGranted.collectAsState()
            val readMediaAudioPermissionGranted by PermissionsManager.readMediaAudioPermissionGranted.collectAsState()
            var displayPermissionsScreen by remember {
                mutableStateOf( !PermissionsManager.hasAllRequiredPermissions.value )
            }

            val settingsRepository = appContainer.settingsRepository
            val themeMode by settingsRepository.themeMode.collectAsState()
            val primaryColorName by settingsRepository.primaryColorName.collectAsState()
            val font by settingsRepository.font.collectAsState()
            val fontScale by settingsRepository.fontScale.collectAsState()
            val useMaterialYou by settingsRepository.useMaterialYou.collectAsState()

            MusicMattersTheme(
                themeMode = themeMode,
                primaryColorName = primaryColorName,
                fontName = font.name,
                fontScale = fontScale,
                useMaterialYou = useMaterialYou
            ) {

                Surface( color = MaterialTheme.colorScheme.background ) {
                    when {
                        displayPermissionsScreen -> {
                            PermissionsScreen(
                                allRequiredPermissionsHaveBeenGranted = allRequiredPermissionsHaveBeenGranted,
                                postNotificationsPermissionGranted = postNotificationsPermissionGranted,
                                onPostNotificationsPermissionGranted = {
                                    PermissionsManager.postNotificationPermissionGranted( it )
                                },
                                readExternalStoragePermissionsGranted = readExternalStoragePermissionGranted,
                                onReadExternalStoragePermissionGranted = {
                                    PermissionsManager.readExternalStoragePermissionGranted( it )
                                },
                                readMediaAudioPermissionGranted = readMediaAudioPermissionGranted,
                                onReadMediaAudioPermissionGranted = {
                                    PermissionsManager.readMediaAudioPermissionGranted( it )
                                },
                                onLetsGo = {
                                    displayPermissionsScreen = false
                                }
                            )
                        }
                        else -> {
                            MusicallyApp(
                                settingsRepository = appContainer.settingsRepository,
                                musicServiceConnection = appContainer.musicServiceConnection,
                                playlistRepository = appContainer.playlistRepository,
                                nowPlayingViewModel = nowPlayingViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
