package com.odesa.musically.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.songs.SongsRepository
import com.odesa.musically.ui.navigation.MusicallyNavHost
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun MusicallyApp(
    navController: NavHostController = rememberNavController(),
    settingsRepository: SettingsRepository,
    songsRepository: SongsRepository
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
                songsRepository = songsRepository,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicallyAppContent(
    navController: NavHostController,
    settingsRepository: SettingsRepository,
    songsRepository: SongsRepository,
) {

    val homeTabs by settingsRepository.homeTabs.collectAsState()
    val language by settingsRepository.language.collectAsState()
    val labelVisibility by settingsRepository.homePageBottomBarLabelVisibility.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MusicallyNavHost(
            modifier = Modifier.weight( 1f ),
            navController = navController,
            settingsRepository = settingsRepository,
            songsRepository = songsRepository,
            visibleTabs = homeTabs,
            language = language,
            labelVisibility = labelVisibility,
        )
    }
}

