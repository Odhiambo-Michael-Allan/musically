package com.odesa.musicMatters.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

class PlaylistScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PlaylistScreenUiState(
            songsInPlaylist = emptyList(),
            isLoadingSongsInPlaylist = true,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = emptyList(),
        )
    )
    val uiState = _uiState.asStateFlow()

    fun loadSongsInPlaylistWithId( playlistId: String ) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songIds = playlistRepository.playlists.value.find { it.id == playlistId }!!.songIds
                _uiState.value = _uiState.value.copy(
                    songsInPlaylist = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                        .filter { songIds.contains( it.mediaId ) }
                        .map { it.toSong( artistTagSeparators ) },
                    isLoadingSongsInPlaylist = false
                )
            }
        }
    }
}

data class PlaylistScreenUiState(
    val songsInPlaylist: List<Song>,
    val isLoadingSongsInPlaylist: Boolean,
    val language: Language,
    val themeMode: ThemeMode,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
)

@Suppress( "UNCHECKED_CAST" )
class PlaylistScreenViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistsRepository: PlaylistRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( PlaylistScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistsRepository
        ) as T )
}