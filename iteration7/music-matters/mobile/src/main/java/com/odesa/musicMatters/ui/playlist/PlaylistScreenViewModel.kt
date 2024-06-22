package com.odesa.musicMatters.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlistId: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val playlistRepository: PlaylistRepository,
    settingsRepository: SettingsRepository,
) : BaseViewModel(
    musicServiceConnection = musicServiceConnection,
    settingsRepository = settingsRepository,
    playlistRepository = playlistRepository
) {

    private var currentPlaylistId: String? = null

    private val _uiState = MutableStateFlow(
        PlaylistScreenUiState(
            playlist = EMPTY_PLAYLIST,
            songsInPlaylist = emptyList(),
            isLoadingSongsInPlaylist = musicServiceConnection.isInitializing.value,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlayingMediaItem.value.mediaId,
            favoriteSongIds = playlistRepository.favoritesPlaylist.value.songIds,
            playlists = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeFavoriteSongIds() }
        addOnPlaylistsChangeListener {
            _uiState.value = _uiState.value.copy( playlists = it )
            // The songs in the current playlist may have changed so fetch them again..
            fetchSongsInPlaylistWithId( playlistId )
        }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect { isInitializing ->
            _uiState.value = _uiState.value.copy(
                isLoadingSongsInPlaylist = isInitializing
            )
            if ( !isInitializing ) fetchSongsInPlaylistWithId( playlistId )
        }
    }

    private suspend fun observeCurrentlyPlayingSong() {
        musicServiceConnection.nowPlayingMediaItem.collect {
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

    private fun fetchSongsInPlaylistWithId(playlistId: String ) {
        currentPlaylistId = playlistId
        playlistRepository.playlists.value.find { it.id == playlistId }?.let { playlist ->
            _uiState.value = _uiState.value.copy(
                playlist = playlist,
                songsInPlaylist = musicServiceConnection.cachedSongs.value
                    .filter { playlist.songIds.contains( it.id ) }
            )
        }
    }

}

data class PlaylistScreenUiState(
    val playlist: Playlist,
    val songsInPlaylist: List<Song>,
    val isLoadingSongsInPlaylist: Boolean,
    val language: Language,
    val themeMode: ThemeMode,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val playlists: List<Playlist>
)

private val EMPTY_PLAYLIST = Playlist(
    id = "",
    title = "",
    songIds = emptyList()
)

internal val testPlaylistScreenUiState = PlaylistScreenUiState(
    playlist = testPlaylists.first(),
    songsInPlaylist = testSongs,
    isLoadingSongsInPlaylist = false,
    language = English,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = emptyList(),
    themeMode = SettingsDefaults.themeMode,
    playlists = emptyList()
)

@Suppress( "UNCHECKED_CAST" )
class PlaylistScreenViewModelFactory(
    private val playlistId: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistsRepository: PlaylistRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( PlaylistScreenViewModel(
            playlistId = playlistId,
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistsRepository
        ) as T )
}