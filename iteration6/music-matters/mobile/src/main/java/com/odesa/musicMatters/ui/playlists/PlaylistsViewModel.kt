package com.odesa.musicMatters.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : BaseViewModel(
    musicServiceConnection = musicServiceConnection,
    settingsRepository = settingsRepository,
    playlistRepository = playlistRepository
) {

    private val _uiState = MutableStateFlow(
        PlaylistsScreenUiState(
            playlists = playlistRepository.playlists.value,
            songs = emptyList(),
            isLoadingSongs = musicServiceConnection.isInitializing.value,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeSongs() }
        viewModelScope.launch { observePlaylistsChange() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isLoadingSongs = it
            )
        }
    }

    private suspend fun observeSongs() {
        musicServiceConnection.cachedSongs.collect {
            _uiState.value = _uiState.value.copy(
                songs = it
            )
        }
    }

    private suspend fun observePlaylistsChange() {
        playlistRepository.playlists.collect {
            _uiState.value = _uiState.value.copy(
                playlists = it
            )
        }
    }

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy (
                language = it
            )
        }
    }

    private suspend fun observeThemeModeChange() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

}

data class PlaylistsScreenUiState(
    val songs: List<Song>,
    val playlists: List<Playlist>,
    val isLoadingSongs: Boolean,
    val language: Language,
    val themeMode: ThemeMode,
)

internal val testPlaylistsScreenUiState = PlaylistsScreenUiState(
    songs = emptyList(),
    playlists = testPlaylists,
    isLoadingSongs = false,
    language = English,
    themeMode = SettingsDefaults.themeMode
)

@Suppress( "UNCHECKED_CAST" )
class PlaylistsViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ( PlaylistsViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        ) as T )
}