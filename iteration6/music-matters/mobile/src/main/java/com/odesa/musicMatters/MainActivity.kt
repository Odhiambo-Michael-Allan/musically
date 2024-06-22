package com.odesa.musicMatters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.odesa.musicMatters.core.common.media.MediaPermissionsManager
import com.odesa.musicMatters.core.designsystem.theme.MusicMattersTheme
import com.odesa.musicMatters.di.MobileDiModule
import com.odesa.musicMatters.ui.MusicallyApp
import com.odesa.musicMatters.ui.components.PermissionsScreen
import com.odesa.musicMatters.ui.nowPlaying.NowPlayingViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private lateinit var mobileDiModule: MobileDiModule
    private lateinit var mediaPermissionsManager: MediaPermissionsManager

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        Timber.plant( Timber.DebugTree() )
        mobileDiModule = ( application as MusicMatters ).diModule
        mediaPermissionsManager = mobileDiModule.mediaPermissionsManager
        mediaPermissionsManager.checkForPermissions( applicationContext )
        val nowPlayingViewModel: NowPlayingViewModel by viewModels {
            NowPlayingViewModel.NowPlayingViewModelFactory(
                application = application,
                musicServiceConnection = mobileDiModule.musicServiceConnection,
                settingsRepository = mobileDiModule.settingsRepository,
                playlistRepository = mobileDiModule.playlistRepository
            )
        }
        enableEdgeToEdge()
        setContent {

            val allRequiredPermissionsHaveBeenGranted by mediaPermissionsManager.hasAllRequiredPermissions.collectAsState()
            val postNotificationsPermissionGranted by mediaPermissionsManager.postNotificationPermissionGranted.collectAsState()
            val readExternalStoragePermissionGranted by mediaPermissionsManager.readExternalStoragePermissionGranted.collectAsState()
            val readMediaAudioPermissionGranted by mediaPermissionsManager.readMediaAudioPermissionGranted.collectAsState()
            var displayPermissionsScreen by remember {
                mutableStateOf( !mediaPermissionsManager.hasAllRequiredPermissions.value )
            }

            val settingsRepository = mobileDiModule.settingsRepository
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
                                    mediaPermissionsManager.postNotificationPermissionGranted(
                                        isGranted = it,
                                        context = applicationContext
                                    )
                                },
                                readExternalStoragePermissionsGranted = readExternalStoragePermissionGranted,
                                onReadExternalStoragePermissionGranted = {
                                    mediaPermissionsManager.readExternalStoragePermissionGranted(
                                        isGranted = it,
                                        context = applicationContext
                                    )
                                },
                                readMediaAudioPermissionGranted = readMediaAudioPermissionGranted,
                                onReadMediaAudioPermissionGranted = {
                                    mediaPermissionsManager.readMediaAudioPermissionGranted(
                                        isGranted = it,
                                        context = applicationContext
                                    )
                                },
                                onLetsGo = {
                                    displayPermissionsScreen = false
                                }
                            )
                        }
                        else -> {
                            MusicallyApp(
                                settingsRepository = mobileDiModule.settingsRepository,
                                musicServiceConnection = mobileDiModule.musicServiceConnection,
                                playlistRepository = mobileDiModule.playlistRepository,
                                searchHistoryRepository = mobileDiModule.searchHistoryRepository,
                                nowPlayingViewModel = nowPlayingViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
