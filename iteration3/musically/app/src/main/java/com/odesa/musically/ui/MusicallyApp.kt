package com.odesa.musically.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.ui.components.NowPlayingBottomBar
import com.odesa.musically.ui.navigation.MusicallyNavHost
import com.odesa.musically.ui.nowPlaying.NowPlayingBottomSheet
import com.odesa.musically.ui.nowPlaying.NowPlayingViewModel
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    musicServiceConnection: MusicServiceConnection,
    nowPlayingViewModel: NowPlayingViewModel
) {
    val themeMode by settingsRepository.themeMode.collectAsState()
    val primaryColorName by settingsRepository.primaryColorName.collectAsState()
    val font by settingsRepository.font.collectAsState()
    val fontScale by settingsRepository.fontScale.collectAsState()
    val useMaterialYou by settingsRepository.useMaterialYou.collectAsState()

    MusicallyTheme(
        themeMode = themeMode,
        primaryColorName = primaryColorName,
        fontName = font.name,
        fontScale = fontScale,
        useMaterialYou = useMaterialYou
    ) {
        Surface( color = MaterialTheme.colorScheme.background ) {
            MusicallyAppContent(
                navController = navController,
                settingsRepository = settingsRepository,
                musicServiceConnection = musicServiceConnection,
                nowPlayingViewModel = nowPlayingViewModel
            )
        }
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    musicServiceConnection: MusicServiceConnection,
    nowPlayingViewModel: NowPlayingViewModel
) {

    val homeTabs by settingsRepository.homeTabs.collectAsState()
    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()

    val nowPlayingBottomBarUiState by nowPlayingViewModel.bottomBarUiState.collectAsState()
    val nowPlayingBottomSheetUiState by nowPlayingViewModel.bottomSheetUiState.collectAsState()
    var showNowPlayingBottomSheet by remember { mutableStateOf( false ) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MusicallyNavHost(
            modifier = Modifier.weight( 1f ),
            navController = navController,
            settingsRepository = settingsRepository,
            musicServiceConnection = musicServiceConnection,
            visibleTabs = homeTabs,
            language = language,
            labelVisibility = labelVisibility,
        ) {
            NowPlayingBottomBar(
                nowPlayingBottomBarUiState = nowPlayingBottomBarUiState,
                onNowPlayingBottomBarSwipeUp = { showNowPlayingBottomSheet = true },
                onNowPlayingBottomBarClick = { showNowPlayingBottomSheet = true },
                nextSong = { nowPlayingViewModel.playNextSong() },
                previousSong = { nowPlayingViewModel.playPreviousSong() },
                seekBack = { nowPlayingViewModel.fastRewind() },
                seekForward = { nowPlayingViewModel.fastForward() },
                playPause = { nowPlayingViewModel.playPause() },
            )

            if ( showNowPlayingBottomSheet ) {
                val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true )

                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { showNowPlayingBottomSheet = false }
                ) {
                    NowPlayingBottomSheet(
                        nowPlayingBottomSheetUiState = nowPlayingBottomSheetUiState,
                        onFavorite = { nowPlayingViewModel.onFavorite( it ) },
                        playPause = { nowPlayingViewModel.playPause() },
                        playPreviousSong = { nowPlayingViewModel.playPreviousSong() },
                        playNextSong = { nowPlayingViewModel.playNextSong() },
                        fastRewind = { nowPlayingViewModel.fastRewind() },
                        fastForward = { nowPlayingViewModel.fastForward() },
                        onSeekEnd = { nowPlayingViewModel.onSeekEnd( it ) },
                        onArtworkClicked = { nowPlayingViewModel.onArtworkClicked() },
                        toggleLoopMode = { nowPlayingViewModel.toggleLoopMode() },
                        toggleShuffleMode = { nowPlayingViewModel.toggleShuffleMode() },
                        togglePauseOnCurrentSongEnd = { nowPlayingViewModel.togglePauseOnCurrentSongEnd() },
                        onPlayingSpeedChange = { nowPlayingViewModel.onPlayingSpeedChange( it ) },
                        onPlayingPitchChange = { nowPlayingViewModel.onPlayingPitchChange( it ) },
                        onCreateEqualizerActivityContract = { emptyActivityResultContract }
                    )
                }

            }
        }
    }
}

val emptyActivityResultContract = object : ActivityResultContract<Unit, Unit>() {
    override fun createIntent( context: Context, input: Unit ) = Intent()
    override fun parseResult( resultCode: Int, intent: Intent? ) {}
}



