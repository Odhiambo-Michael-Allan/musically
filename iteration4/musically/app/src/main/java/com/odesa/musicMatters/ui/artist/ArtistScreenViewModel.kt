package com.odesa.musicMatters.ui.artist

import androidx.lifecycle.ViewModel
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection

class ArtistScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
}