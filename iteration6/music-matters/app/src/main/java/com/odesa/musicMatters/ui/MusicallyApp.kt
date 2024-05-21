package com.odesa.musicMatters.ui

import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.search.SearchHistoryRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.components.NowPlayingBottomBar
import com.odesa.musicMatters.ui.navigation.MusicMattersNavHost
import com.odesa.musicMatters.ui.navigation.Route
import com.odesa.musicMatters.ui.navigation.navigateToAlbumScreen
import com.odesa.musicMatters.ui.navigation.navigateToArtistScreen
import com.odesa.musicMatters.ui.nowPlaying.NowPlayingBottomSheet
import com.odesa.musicMatters.ui.nowPlaying.NowPlayingViewModel
import timber.log.Timber

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    searchHistoryRepository: SearchHistoryRepository,
    musicServiceConnection: MusicServiceConnection,
    nowPlayingViewModel: NowPlayingViewModel
) {
    MusicallyAppContent(
        navController = navController,
        settingsRepository = settingsRepository,
        playlistRepository = playlistRepository,
        musicServiceConnection = musicServiceConnection,
        searchHistoryRepository = searchHistoryRepository,
        nowPlayingViewModel = nowPlayingViewModel
    )
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    searchHistoryRepository: SearchHistoryRepository,
    musicServiceConnection: MusicServiceConnection,
    nowPlayingViewModel: NowPlayingViewModel
) {

    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()
    val nowPlayingScreenUiState by nowPlayingViewModel.uiState.collectAsState()
    var showNowPlayingBottomSheet by remember { mutableStateOf( false ) }

    val packageName = LocalContext.current.packageName
    val equalizerActivity = rememberLauncherForActivityResult( object : ActivityResultContract<Unit, Unit>() {
        override fun createIntent( context: Context, input: Unit ) = Intent(
            AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL
        ).apply {
            putExtra( AudioEffect.EXTRA_PACKAGE_NAME, packageName )
            putExtra( AudioEffect.EXTRA_AUDIO_SESSION, 0 )
            putExtra( AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC )
        }

        override fun parseResult( resultCode: Int, intent: Intent? ) {}

    } ) {}

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MusicMattersNavHost(
            modifier = Modifier.weight( 1f ),
            navController = navController,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
            searchHistoryRepository = searchHistoryRepository,
            musicServiceConnection = musicServiceConnection,
            language = language,
            labelVisibility = labelVisibility,
        ) {
            NowPlayingBottomBar(
                nowPlayingScreenUiState = nowPlayingScreenUiState,
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
                        uiState = nowPlayingScreenUiState,
                        onFavorite = { nowPlayingViewModel.addToFavorites( it ) },
                        playPause = { nowPlayingViewModel.playPause() },
                        playPreviousSong = { nowPlayingViewModel.playPreviousSong() },
                        playNextSong = { nowPlayingViewModel.playNextSong() },
                        fastRewind = { nowPlayingViewModel.fastRewind() },
                        fastForward = { nowPlayingViewModel.fastForward() },
                        onSeekEnd = { nowPlayingViewModel.onSeekEnd( it ) },
                        onArtworkClicked = {
                            navController.navigateToAlbumScreen( it )
                            showNowPlayingBottomSheet = false
                        },
                        onArtistClicked = {
                            navController.navigateToArtistScreen( it )
                            showNowPlayingBottomSheet = false
                        },
                        toggleLoopMode = { nowPlayingViewModel.toggleLoopMode() },
                        toggleShuffleMode = { nowPlayingViewModel.toggleShuffleMode() },
                        onPlayingSpeedChange = { nowPlayingViewModel.onPlayingSpeedChange( it ) },
                        onPlayingPitchChange = { nowPlayingViewModel.onPlayingPitchChange( it ) },
                        onQueueClicked = {
                            showNowPlayingBottomSheet = false
                            navController.navigate( Route.Queue.name ) {
                                launchSingleTop = true
                            }
                        },
                        onCreateEqualizerActivityContract = {
                            try {
                                equalizerActivity.launch()
                            } catch ( exception: Exception ) {
                                Timber.tag( "NOW-PLAYING-BOTTOM-BAR" ).d(
                                    "Launching equalizer failed: $exception"
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}




