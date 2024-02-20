package com.odesa.musically.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odesa.musically.MainActivityViewModel
import com.odesa.musically.NowPlayingUiState
import com.odesa.musically.R
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.NOTHING_PLAYING
import com.odesa.musically.services.media.extensions.toSong
import com.odesa.musically.ui.components.NowPlayingBottomBar
import com.odesa.musically.ui.components.PlaybackPosition
import com.odesa.musically.ui.navigation.MusicallyNavHost
import com.odesa.musically.ui.theme.MusicallyTheme
import com.odesa.musically.ui.theme.isLight
import java.util.UUID

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    musicServiceConnection: MusicServiceConnection,
    mainActivityViewModel: MainActivityViewModel
) {
    val themeMode by settingsRepository.themeMode.collectAsState()
    val primaryColorName by settingsRepository.primaryColorName.collectAsState()
    val font by settingsRepository.font.collectAsState()
    val fontScale by settingsRepository.fontScale.collectAsState()
    val useMaterialYou by settingsRepository.useMaterialYou.collectAsState()
    val nowPlayingUiState by mainActivityViewModel.nowPlayingUiState.collectAsState()

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
                nowPlayingUiState = nowPlayingUiState
            )
        }
    }
}

@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    musicServiceConnection: MusicServiceConnection,
    nowPlayingUiState: NowPlayingUiState
) {

    val homeTabs by settingsRepository.homeTabs.collectAsState()
    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()
    val textMarquee by settingsRepository.miniPlayerTextMarquee.collectAsState()
    val showTrackControls by settingsRepository.miniPlayerShowTrackControls.collectAsState()
    val showSeekControls by settingsRepository.miniPlayerShowSeekControls.collectAsState()
    val nowPlayingMediaItem by musicServiceConnection.nowPlaying.collectAsState()
    val themeMode by settingsRepository.themeMode.collectAsState()
    val playbackState by musicServiceConnection.playbackState.collectAsState()
    val player = musicServiceConnection.player

    val fallbackResourceId =
        if ( themeMode.isLight( LocalContext.current ) )
            R.drawable.placeholder_light else R.drawable.placeholder


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
                currentlyPlayingSong = if ( nowPlayingMediaItem == NOTHING_PLAYING ) null else nowPlayingMediaItem.toSong(),
                playbackPosition = PlaybackPosition( nowPlayingUiState.currentMediaPosition, nowPlayingUiState.currentMediaDuration ),
                onNowPlayingBottomBarSwipeUp = { /*TODO*/ },
                onNowPlayingBottomBarSwipeDown = { /*TODO*/ },
                onNowPlayingBottomBarClick = { /*TODO*/ },
                fallbackResourceId = fallbackResourceId,
                nextSong = {
                    player?.let {
                        it.seekToNext()
                        true
                    } ?: false
                },
                previousSong = {
                    player?.let {
                        it.seekToPrevious()
                        true
                    } ?: false
                },
                textMarquee = textMarquee,
                showTrackControls = showTrackControls,
                showSeekControls = showSeekControls,
                seekBack = { player?.seekBack() },
                seekForward = { player?.seekForward() },
                playPause = { if ( playbackState.isPlaying ) player?.pause() else player?.play() },
                isPlaying = playbackState.isPlaying
            )
        }
    }
}

val testSongs = List( 100 ) {
    Song(
        id = UUID.randomUUID().toString(),
        title = "You Right",
        displayTitle = "You Right",
        trackNumber = 1,
        year = 0,
        duration = 180000L,
        albumTitle = "Best of Levels",
        artist = "The Weekend",
        composers = "Abel \"The Weekend\" Tesfaye",
        dateModified = 1000000,
        size = 1000,
        path = "/storage/emulated/0/Music/Telegram/DojaCat - You Right.mp3",
        mediaUri = Uri.EMPTY,
        artworkUri = Uri.EMPTY,
        mediaItem = MediaItem.EMPTY
    )
}

