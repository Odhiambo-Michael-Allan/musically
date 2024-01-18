package com.odesa.musically

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.odesa.musically.ui.settings.SettingsViewModel
import com.odesa.musically.ui.settings.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        val appContainer = ( application as MusicallyApplication ).container
        val viewModel by viewModels<SettingsViewModel> {
            SettingsViewModelFactory( appContainer.settingRepository )
        }

        setContent {
//            MusicallyTheme {
//                val uiState by viewModel.uiState.collectAsState()
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    SettingsScreenContent(
//                        uiState = uiState,
//                        onLanguageChange = { localeCode -> viewModel.setLanguage( localeCode ) },
//                        onFontChange = {}
//                    )
//                }
//            }
        }
    }
}
