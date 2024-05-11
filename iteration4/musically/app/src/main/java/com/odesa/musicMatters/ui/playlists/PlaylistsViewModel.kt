package com.odesa.musicMatters.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PlaylistsScreenUiState(
            playlists = playlistRepository.playlists.value,
            songs = emptyList(),
            isLoadingSongs = true,
            language = settingsRepository.language.value
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        fetchSongs()
        viewModelScope.launch { observePlaylistsChange() }
        viewModelScope.launch { observeLanguageChange() }
    }

    private fun fetchSongs() {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT ).map {
                    it.toSong( artistTagSeparators )
                }
                _uiState.value = _uiState.value.copy(
                    songs = songs,
                    isLoadingSongs = false
                )
            }
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

    fun playSongsInPlaylist( songIds: List<String> ) {
        if ( !musicServiceConnection.isInitialized || songIds.isEmpty() ) return
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT ).filter {
                    songIds.contains( it.mediaId )
                }
                musicServiceConnection.playMediaItem(
                    mediaItem = songs.first(),
                    mediaItems = songs,
                    shuffle = settingsRepository.shuffle.value
                )
            }
        }
    }

}

data class PlaylistsScreenUiState(
    val songs: List<Song>,
    val playlists: List<Playlist>,
    val isLoadingSongs: Boolean,
    val language: Language,
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