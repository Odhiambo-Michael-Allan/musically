package com.odesa.musicMatters.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
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
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeAlbums() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
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

    fun playAlbum( albumName: String ) {
        viewModelScope.launch {
            val songs = musicServiceConnection.cachedSongs.value
            val songsInAlbum = songs.filter { it.albumTitle == albumName }.map { it.mediaItem }
            musicServiceConnection.playMediaItem(
                mediaItem = songsInAlbum.first(),
                mediaItems = songsInAlbum,
                shuffle = settingsRepository.shuffle.value
            )
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