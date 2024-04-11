package com.odesa.musicMatters.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSICALLY_TRACKS_ROOT
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QueueScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        QueueScreenUiState(
            songsInQueue = emptyList(),
            language = settingsRepository.language.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            themeMode = settingsRepository.themeMode.value,
            isLoading = true
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { fetchMediaItems() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeThemeMode() }
    }

    private suspend fun fetchMediaItems() {
        val playlist = musicServiceConnection.getChildren( MUSICALLY_TRACKS_ROOT )
        _uiState.value = _uiState.value.copy(
            songsInQueue = playlist.map { it.toSong( artistTagSeparators ) },
            isLoading = false
        )
    }

    private suspend fun observeCurrentlyPlayingSong() {
        musicServiceConnection.nowPlaying.collect {
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSongId = it.mediaId
            )
        }
    }

    private suspend fun observeThemeMode() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }
}

data class QueueScreenUiState(
    val songsInQueue: List<Song>,
    val currentlyPlayingSongId: String,
    val language: Language,
    val themeMode: ThemeMode,
    val isLoading: Boolean
)

@Suppress( "UNCHECKED_CAST" )
class QueueScreenViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( QueueScreenViewModel(
            settingsRepository,
            musicServiceConnection,
        ) as T )
}