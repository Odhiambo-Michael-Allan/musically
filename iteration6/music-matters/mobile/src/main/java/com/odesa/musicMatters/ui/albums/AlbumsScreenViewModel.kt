package com.odesa.musicMatters.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Album
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumsScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : BaseViewModel(
    musicServiceConnection = musicServiceConnection,
    settingsRepository = settingsRepository,
    playlistRepository = playlistRepository
) {

    private val _uiState = MutableStateFlow(
        AlbumsScreenUiState(
            albums = emptyList(),
            isLoadingAlbums = true,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            playlists = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeAlbums() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
        addOnPlaylistsChangeListener {
            _uiState.value = _uiState.value.copy(
                playlists = it
            )
        }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isLoadingAlbums = it
            )
        }
    }

    private suspend fun observeAlbums() {
        musicServiceConnection.cachedAlbums.collect {
            _uiState.value = _uiState.value.copy(
                albums = it
            )
        }
    }

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
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

data class AlbumsScreenUiState(
    val albums: List<Album>,
    val isLoadingAlbums: Boolean,
    val language: Language,
    val themeMode: ThemeMode,
    val playlists: List<Playlist>
)

internal val testAlbumsScreenUiState =
    AlbumsScreenUiState(
        albums = List( 10 ) {
            Album(
                title = "Album-$it",
                artists = setOf( "Travis Scott", "Doja Cat" ),
                artworkUri = null
            )
        },
        isLoadingAlbums = false,
        language = English,
        themeMode = SettingsDefaults.themeMode,
        playlists = emptyList()
    )

@Suppress( "UNCHECKED_CAST" )
class AlbumsViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( AlbumsScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        ) as T )
}