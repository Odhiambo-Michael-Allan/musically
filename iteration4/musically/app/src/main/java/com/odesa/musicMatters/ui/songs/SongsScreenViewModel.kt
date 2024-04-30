package com.odesa.musicMatters.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongsScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private var playlist = emptyList<MediaItem>()

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList(),
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = emptyList(),
            isLoading = true
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        fetchSongs()
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeFavoriteSongIds() }
    }

    private fun fetchSongs() {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                playlist = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                _uiState.value = _uiState.value.copy(
                    songs = playlist.map { it.toSong( artistTagSeparators ) }
                )
                _uiState.value = _uiState.value.copy (
                    isLoading = false
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

    private suspend fun observeThemeMode() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeCurrentlyPlayingSong() {
        musicServiceConnection.nowPlaying.collect {
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSongId = it.mediaId
            )
        }
    }

    private suspend fun observeFavoriteSongIds() {
        playlistRepository.favoritesPlaylist.collect {
            _uiState.value = _uiState.value.copy(
                favoriteSongIds = it.songIds
            )
        }
    }

    fun addToFavorites( songId: String ) {
        viewModelScope.launch {
            if ( playlistRepository.isFavorite( songId ) )
                playlistRepository.removeFromFavorites( songId )
            else
                playlistRepository.addToFavorites( songId )
        }
    }

    fun playMedia(
        mediaItem: MediaItem,
    ) {
        viewModelScope.launch {
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = uiState.value.songs.map { it.mediaItem },
                shuffle = settingsRepository.shuffle.value
            )
        }
    }
}

data class SongsScreenUiState(
    val language: Language,
    val themeMode: ThemeMode,
    val songs: List<Song>,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val isLoading: Boolean
)

@Suppress( "UNCHECKED_CAST" )
class SongsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SongsScreenViewModel(
            settingsRepository,
            playlistRepository,
            musicServiceConnection,
        ) as T )
}

const val SongsViewModelTag = "SONGS-VIEW-MODEL"