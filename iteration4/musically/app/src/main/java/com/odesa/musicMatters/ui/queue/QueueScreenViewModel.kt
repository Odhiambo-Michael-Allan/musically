package com.odesa.musicMatters.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class QueueScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        QueueScreenUiState(
            songsInQueue = emptyList(),
            language = settingsRepository.language.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            currentlyPlayingSongIndex = musicServiceConnection.currentlyPlayingMediaItemIndex.value,
            themeMode = settingsRepository.themeMode.value,
            favoriteSongIds = emptySet(),
            isLoading = true
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMediaItems() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeCurrentlyPlayingSongIndex() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeFavoriteSongIds() }
    }

    private suspend fun observeMediaItems() {
        musicServiceConnection.mediaItemsInQueue.collect { mediaItems ->
            _uiState.value = _uiState.value.copy(
                songsInQueue = mediaItems.map { it.toSong( artistTagSeparators ) },
                isLoading = false
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

    private suspend fun observeCurrentlyPlayingSongIndex() {
        musicServiceConnection.currentlyPlayingMediaItemIndex.collect {
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSongIndex = it
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

    fun addToFavorites( songId: String ) {
        viewModelScope.launch {
            if ( playlistRepository.isFavorite( songId ) )
                playlistRepository.removeFromFavorites( songId )
            else
                playlistRepository.addToFavorites( songId )
        }
    }

    private suspend fun observeFavoriteSongIds() {
        playlistRepository.favoritesPlaylist.collect {
            _uiState.value = _uiState.value.copy(
                favoriteSongIds = it.songIds
            )
        }
    }

    fun moveSong( from: Int, to: Int ) {
        musicServiceConnection.moveMediaItem( from, to )
    }

    fun clearQueue() {
        musicServiceConnection.clearQueue()
    }

    fun saveCurrentPlaylist( playlistName: String ) {
        val playlist = Playlist(
            id = UUID.randomUUID().toString(),
            title = playlistName,
            songIds = uiState.value.songsInQueue.map { it.id }.toSet(),
            numberOfTracks = uiState.value.songsInQueue.size
        )
        viewModelScope.launch {
            playlistRepository.savePlaylist( playlist )
        }
    }

    fun playMedia(
        mediaItem: MediaItem
    ) {
        viewModelScope.launch {
            musicServiceConnection.playMediaItem(
                mediaItem,
                uiState.value.songsInQueue.map { it.mediaItem },
                shuffle = false
            )
        }
    }
}

data class QueueScreenUiState(
    val songsInQueue: List<Song>,
    val currentlyPlayingSongId: String,
    val currentlyPlayingSongIndex: Int,
    val language: Language,
    val themeMode: ThemeMode,
    val favoriteSongIds: Set<String>,
    val isLoading: Boolean
)

@Suppress( "UNCHECKED_CAST" )
class QueueScreenViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( QueueScreenViewModel(
            settingsRepository,
            playlistRepository,
            musicServiceConnection,
        ) as T )
}