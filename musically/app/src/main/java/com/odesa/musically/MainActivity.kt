package com.odesa.musically

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.odesa.musically.ui.MusicallyApp
import com.odesa.musically.ui.settings.SettingsViewModel
import com.odesa.musically.ui.settings.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        enableEdgeToEdge()
        super.onCreate( savedInstanceState )

        val appContainer = ( application as MusicallyApplication ).container
        val settingsViewModel by viewModels<SettingsViewModel> {
            SettingsViewModelFactory( appContainer.settingRepository )
        }

        setContent {
            MusicallyApp(
                settingsViewModel = settingsViewModel
            )
        }
    }
}
