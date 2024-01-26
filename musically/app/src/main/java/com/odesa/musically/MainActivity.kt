package com.odesa.musically

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.odesa.musically.ui.MusicallyApp

class MainActivity : ComponentActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        enableEdgeToEdge()
        super.onCreate( savedInstanceState )
        val appContainer = ( application as MusicallyApplication ).container

        setContent {
            MusicallyApp( appContainer.settingsRepository )
        }
    }
}
