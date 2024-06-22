package com.odesa.musicMatters.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.search.SearchHistoryRepository
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.ui.navigation.MusicMattersNavHost

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    searchHistoryRepository: SearchHistoryRepository,
    musicServiceConnection: MusicServiceConnection,
) {
    MusicallyAppContent(
        navController = navController,
        settingsRepository = settingsRepository,
        playlistRepository = playlistRepository,
        musicServiceConnection = musicServiceConnection,
        searchHistoryRepository = searchHistoryRepository,
    )
}

@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    playlistRepository: PlaylistRepository,
    searchHistoryRepository: SearchHistoryRepository,
    musicServiceConnection: MusicServiceConnection,
) {

    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MusicMattersNavHost(
            navController = navController,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
            searchHistoryRepository = searchHistoryRepository,
            musicServiceConnection = musicServiceConnection,
            language = language,
            labelVisibility = labelVisibility,
        )
    }
}




