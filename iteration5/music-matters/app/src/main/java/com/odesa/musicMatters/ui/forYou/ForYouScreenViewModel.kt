package com.odesa.musicMatters.ui.forYou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.theme.ThemeMode
import com.odesa.musicMatters.utils.subListNonStrict
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForYouScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val playlistRepository: PlaylistRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ForYouScreenUiState(
            isLoadingRecentSongs = true,
            recentlyAddedSongs = emptyList(),
            isLoadingSuggestedAlbums = true,
            suggestedAlbums = emptyList(),
            isLoadingMostPlayedSongs = true,
            mostPlayedSongs = emptyList(),
            isLoadingSuggestedArtists = true,
            suggestedArtists = emptyList(),
            isLoadingRecentlyPlayedSongs = true,
            recentlyPlayedSongs = emptyList(),
            language = English,
            themeMode = SettingsDefaults.themeMode,
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeRecentlyAddedSongs() }
        viewModelScope.launch { observeSuggestedAlbums() }
        viewModelScope.launch { observeMostPlayedSongsMap() }
        viewModelScope.launch { observeSuggestedArtists() }
        viewModelScope.launch { observeRecentlyPlayedSongsPlaylist() }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isLoadingRecentSongs = it,
                isLoadingSuggestedAlbums = it,
                isLoadingSuggestedArtists = it,
                isLoadingMostPlayedSongs = it,
                isLoadingRecentlyPlayedSongs = it
            )
            fetchMostPlayedSongsUsing( playlistRepository.mostPlayedSongsMap.value )
            fetchRecentlyPlayedSongsUsing( playlistRepository.recentlyPlayedSongsPlaylist.value )
        }
    }

    private suspend fun observeRecentlyAddedSongs() {
        musicServiceConnection.cachedRecentlyAddedSongs.collect {
            _uiState.value = _uiState.value.copy(
                recentlyAddedSongs = it.subListNonStrict( 5 )
            )
        }
    }

    private suspend fun observeSuggestedAlbums() {
        musicServiceConnection.cachedSuggestedAlbums.collect {
            _uiState.value = _uiState.value.copy(
                suggestedAlbums = it
            )
        }
    }

    private suspend fun observeMostPlayedSongsMap() {
        playlistRepository.mostPlayedSongsMap.collect {
            fetchMostPlayedSongsUsing( it )
        }
    }

    private fun fetchMostPlayedSongsUsing( mostPlayedSongsMap: Map<String, Int>) {
        val songs = musicServiceConnection.cachedSongs.value
        val sortedMap = mostPlayedSongsMap.toList().sortedByDescending { it.second }.toMap()
        val mostPlayedSongs = sortedMap.keys.mapNotNull { key ->
            songs.find { it.id == key }
        }
        _uiState.value = _uiState.value.copy(
            mostPlayedSongs = mostPlayedSongs.subListNonStrict( 5 )
        )
    }

    private suspend fun observeSuggestedArtists() {
        musicServiceConnection.cachedSuggestedArtists.collect {
            _uiState.value = _uiState.value.copy(
                suggestedArtists = it
            )
        }
    }

    private suspend fun observeRecentlyPlayedSongsPlaylist() {
        playlistRepository.recentlyPlayedSongsPlaylist.collect {
            fetchRecentlyPlayedSongsUsing( it )
        }
    }

    private fun fetchRecentlyPlayedSongsUsing( playlist: Playlist ) {
        val songs = musicServiceConnection.cachedSongs.value
        val songsInPlaylist = mutableListOf<Song>()
        playlist.songIds.forEach { songId ->
            songs.find { it.id == songId }?.let {
                songsInPlaylist.add( it )
            }
        }
        _uiState.value = _uiState.value.copy(
            recentlyPlayedSongs = songsInPlaylist.subListNonStrict( 5 ),
        )
    }

    fun shuffleAndPlay() {
        if ( uiState.value.isLoadingRecentSongs ) return
        viewModelScope.launch {
            val mediaItems = musicServiceConnection.cachedSongs.value.map { it.mediaItem }
            musicServiceConnection.shuffleAndPlay( mediaItems )
        }
    }

    fun playRecentlyAddedSong( mediaItem: MediaItem) {
        viewModelScope.launch {
            val recentlyAddedSongs = musicServiceConnection.cachedRecentlyAddedSongs.value
            musicServiceConnection.playMediaItem(
                mediaItem,
                recentlyAddedSongs.map { it.mediaItem },
                shuffle = false
            )
        }
    }

    fun playMostPlayedSong( mediaItem: MediaItem ) {
        viewModelScope.launch {
            val mostPlayedSongsMap = playlistRepository.mostPlayedSongsMap.value
            val songs = musicServiceConnection.cachedSongs.value
            val sortedMap = mostPlayedSongsMap.toList().sortedByDescending { it.second }.toMap()
            val mostPlayedSongs = sortedMap.keys.mapNotNull { key ->
                songs.find { it.id == key }
            }
            musicServiceConnection.playMediaItem(
                mediaItem,
                mostPlayedSongs.map { it.mediaItem },
                shuffle = false
            )
        }
    }

    fun playSongInPlayHistory( mediaItem: MediaItem ) {
        viewModelScope.launch {
            val playHistoryPlaylist = playlistRepository.recentlyPlayedSongsPlaylist.value
            val songs = musicServiceConnection.cachedSongs.value
            val songsInPlaylist = mutableListOf<Song>()
            playHistoryPlaylist.songIds.forEach { songId ->
                songs.find { it.id == songId }?.let {
                    songsInPlaylist.add( it )
                }
            }
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = songsInPlaylist.map { it.mediaItem },
                shuffle = false
            )
        }
    }
}

data class ForYouScreenUiState(
    val language: Language,
    val themeMode: ThemeMode,
    val isLoadingRecentSongs: Boolean,
    val recentlyAddedSongs: List<Song>,
    val isLoadingSuggestedAlbums: Boolean,
    val suggestedAlbums: List<Album>,
    val isLoadingMostPlayedSongs: Boolean,
    val mostPlayedSongs: List<Song>,
    val isLoadingSuggestedArtists: Boolean,
    val suggestedArtists: List<Artist>,
    val isLoadingRecentlyPlayedSongs: Boolean,
    val recentlyPlayedSongs: List<Song>,
)

@Suppress( "UNCHECKED_CAST" )
class ForYouViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val playlistRepository: PlaylistRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {
     override fun <T : ViewModel> create(modelClass: Class<T> ) =
        ( ForYouScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            playlistRepository = playlistRepository,
            settingsRepository = settingsRepository,
        ) as T )
}

const val TAG = "FOR-YOU-VIEW-MODEL-TAG"