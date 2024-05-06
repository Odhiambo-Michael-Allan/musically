package com.odesa.musicMatters.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toAlbum
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_ALBUMS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumsScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AlbumsScreenUiState(
            albums = emptyList(),
            isLoadingAlbums = true,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        fetchAlbums()
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
    }

    private fun fetchAlbums() {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val albums = musicServiceConnection.getChildren( MUSIC_MATTERS_ALBUMS_ROOT )
                _uiState.value = _uiState.value.copy(
                    albums = albums.map { it.toAlbum() },
                    isLoadingAlbums = false
                )
            }
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

    fun playAlbum( albumName: String ) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                val songsInAlbum = songs.filter { it.mediaMetadata.albumTitle == albumName }
                musicServiceConnection.playMediaItem(
                    mediaItem = songsInAlbum.first(),
                    mediaItems = songsInAlbum,
                    shuffle = settingsRepository.shuffle.value
                )
            }
        }
    }
}

data class AlbumsScreenUiState(
    val albums: List<Album>,
    val isLoadingAlbums: Boolean,
    val language: Language,
    val themeMode: ThemeMode,
)

@Suppress( "UNCHECKED_CAST" )
class AlbumsViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( AlbumsScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository
        ) as T )
}