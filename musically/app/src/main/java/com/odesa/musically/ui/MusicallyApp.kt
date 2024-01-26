package com.odesa.musically.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.ui.helpers.FadeTransition
import com.odesa.musically.ui.helpers.Route
import com.odesa.musically.ui.helpers.ScaleTransition
import com.odesa.musically.ui.home.HomeScreen
import com.odesa.musically.ui.settings.SettingsScreen
import com.odesa.musically.ui.settings.SettingsViewModel
import com.odesa.musically.ui.settings.SettingsViewModelFactory
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun MusicallyApp(
    settingsRepository: SettingsRepository,
    navController: NavHostController = rememberNavController()
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
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.Home.name
            ) {
                composable(
                    Route.Home.name,
                    enterTransition = { FadeTransition.enterTransition() }
                ) {
                    HomeScreen(
                        onSettingsClicked = {
                            navController.navigate( Route.Settings.name )
                        }
                    )
                }
                composable(
                    Route.Settings.name,
                    enterTransition= { ScaleTransition.scaleDown.enterTransition() },
                    exitTransition = { ScaleTransition.scaleUp.exitTransition() }
                ) {
                    val settingsViewModel: SettingsViewModel = viewModel(
                        factory = SettingsViewModelFactory(
                            settingsRepository
                        )
                    )
                    SettingsScreen(
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}