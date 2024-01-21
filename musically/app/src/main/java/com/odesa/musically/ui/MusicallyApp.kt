package com.odesa.musically.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.odesa.musically.ui.settings.SettingsScreenContent
import com.odesa.musically.ui.settings.SettingsViewModel
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun MusicallyApp(
    settingsViewModel: SettingsViewModel
) {

    val settingsScreenUiState by settingsViewModel.uiState.collectAsState()

    MusicallyTheme(
        uiState = settingsScreenUiState
    ) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsScreenContent(
                uiState = settingsScreenUiState,
                onLanguageChange = {
                    newLanguage -> settingsViewModel.setLanguage( newLanguage.locale )
                },
                onFontChange = {
                    newFont -> settingsViewModel.setFont( newFont.name )
                },
                onFontScaleChange = {
                    newFontScale -> settingsViewModel.setFontScale( newFontScale )
                },
                onThemeChange = {
                    newTheme -> settingsViewModel.setThemeMode( newTheme )
                },
                onUseMaterialYouChange = {
                    useMaterialYou -> settingsViewModel.setUseMaterialYou( useMaterialYou )
                },
                onPrimaryColorChange = {
                    primaryColorName -> settingsViewModel.setPrimaryColorName( primaryColorName )
                },
                onHomeTabsChange = {
                    homeTabs -> settingsViewModel.setHomeTabs( homeTabs )
                },
                onForYouContentChange = {
                    forYouContent -> settingsViewModel.setForYouContent( forYouContent )
                },
                onHomePageBottomBarLabelVisibilityChange = {
                    value -> settingsViewModel.setHomePageBottomBarLabelVisibility( value )
                }
            )
        }
    }
}