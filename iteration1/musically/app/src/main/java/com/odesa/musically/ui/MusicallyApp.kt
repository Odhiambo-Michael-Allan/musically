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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odesa.musically.R
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.ui.components.NowPlayingBottomBar
import com.odesa.musically.ui.components.PlaybackPosition
import com.odesa.musically.ui.navigation.MusicallyNavHost
import com.odesa.musically.ui.theme.MusicallyTheme
import java.util.UUID

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    musicServiceConnection: MusicServiceConnection,
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
            )
        }
    }
}

@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    musicServiceConnection: MusicServiceConnection
) {

    val homeTabs by settingsRepository.homeTabs.collectAsState()
    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()
    val textMarquee by settingsRepository.miniPlayerTextMarquee.collectAsState()
    val showTrackControls by settingsRepository.miniPlayerShowTrackControls.collectAsState()
    val showSeekControls by settingsRepository.miniPlayerShowSeekControls.collectAsState()

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
                currentlyPlayingSong = testSongs.first(),
                playbackPosition = PlaybackPosition( 3, 5 ),
                onNowPlayingBottomBarSwipeUp = { /*TODO*/ },
                onNowPlayingBottomBarSwipeDown = { /*TODO*/ },
                onNowPlayingBottomBarClick = { /*TODO*/ },
                loadSongArtwork = { R.drawable.placeholder },
                nextSong = { true },
                previousSong = { true },
                textMarquee = textMarquee,
                showTrackControls = showTrackControls,
                showSeekControls = showSeekControls,
                seekBack = {},
                seekForward = {},
                playPause = {},
                isPlaying = true
            )
        }
    }
}

val testSongs = List( 100 ) {
    Song(
        id = UUID.randomUUID().toString(),
        title = "You Right",
        trackNumber = 1L,
        year = 0L,
        duration = 180000L,
        album = "Best of Levels",
        artists = "The Weekend",
        composers = "Abel \"The Weekend\" Tesfaye",
        dateModified = 1000000,
        size = 1000,
        path = "/storage/emulated/0/Music/Telegram/DojaCat - You Right.mp3",
        mediaUri = Uri.EMPTY,
        artworkUri = Uri.EMPTY
    )
}

