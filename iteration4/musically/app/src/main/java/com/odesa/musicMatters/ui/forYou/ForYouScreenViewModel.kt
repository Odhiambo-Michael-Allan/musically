package com.odesa.musicMatters.ui.forYou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toAlbum
import com.odesa.musicMatters.services.media.extensions.toArtist
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_RECENT_SONGS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random

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
            isLoadingPlayHistory = true,
            songsInPlayHistory = emptyList(),
            language = English,
            themeMode = SettingsDefaults.themeMode,
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        fetchRecentlyAddedSongs()
        fetchSuggestedAlbums()
        viewModelScope.launch { observeMostPlayedSongsMap() }
        fetchSuggestedArtists()
        viewModelScope.launch { observeRecentlyPlayedSongsPlaylist() }
    }

    private fun fetchRecentlyAddedSongs() {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val recentlyAddedSongs = musicServiceConnection.getChildren( MUSIC_MATTERS_RECENT_SONGS_ROOT )
                    .map { it.toSong( artistTagSeparators ) }
                _uiState.value = uiState.value.copy(
                    recentlyAddedSongs = recentlyAddedSongs,
                    isLoadingRecentSongs = false
                )
                Timber.tag( TAG ).d( "RECENTLY ADDED SONGS SIZE: ${recentlyAddedSongs.size}")
            }
        }
    }

    private fun fetchSuggestedAlbums() {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val suggestedAlbums = musicServiceConnection.getChildren( MUSIC_MATTERS_SUGGESTED_ALBUMS_ROOT )
                    .map { it.toAlbum() }
                _uiState.value = _uiState.value.copy(
                    suggestedAlbums = suggestedAlbums,
                    isLoadingSuggestedAlbums = false
                )
            }
        }
    }

    private suspend fun observeMostPlayedSongsMap() {
        playlistRepository.mostPlayedSongsMap.collect {
            fetchMostPlayedSongs( it )
        }
    }

    private fun fetchMostPlayedSongs( mostPlayedSongsMap: Map<String, Int>) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                val sortedMap = mostPlayedSongsMap.toList().sortedByDescending { it.second }.toMap()
                val mostPlayedSongs = sortedMap.keys.mapNotNull { key ->
                    songs.find { it.mediaId == key }?.toSong( artistTagSeparators )
                }
                _uiState.value = _uiState.value.copy(
                    mostPlayedSongs = if ( mostPlayedSongs.size >= 5 ) mostPlayedSongs.subList( 0, 5 ) else mostPlayedSongs,
                    isLoadingMostPlayedSongs = false
                )
            }
        }
    }

    private fun fetchSuggestedArtists() {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val suggestedArtists = musicServiceConnection.getChildren( MUSIC_MATTERS_SUGGESTED_ARTISTS_ROOT )
                    .map { it.toArtist() }
                _uiState.value = _uiState.value.copy(
                    suggestedArtists = suggestedArtists,
                    isLoadingSuggestedArtists = false
                )

            }
        }
    }

    private suspend fun observeRecentlyPlayedSongsPlaylist() {
        playlistRepository.recentlyPlayedSongsPlaylist.collect {
            Timber.tag( TAG ).d( "UPDATING RECENTLY PLAYED SONGS PLAYLIST IN FOR VIEW MODEL" )
            fetchRecentlyPlayedSongs( it )
        }
    }

    private fun fetchRecentlyPlayedSongs( playlist: Playlist ) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                val songsInPlaylist = mutableListOf<Song>()
                playlist.songIds.forEach { songId ->
                    songs.find { it.mediaId == songId }?.let {
                        songsInPlaylist.add( it.toSong( artistTagSeparators ) )
                    }
                }
                _uiState.value = _uiState.value.copy(
                    songsInPlayHistory = if ( songsInPlaylist.size >= 5 ) songsInPlaylist.subList( 0, 5 ) else songsInPlaylist,
                    isLoadingPlayHistory = false
                )
            }
        }
    }

    fun shuffleAndPlay() {
        if ( uiState.value.isLoadingRecentSongs ) return
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                settingsRepository.setShuffle( true )
                val songsList = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                    .map { it.toSong( artistTagSeparators ) }
                val randomSongToPlay = songsList[ Random.nextInt( songsList.size ) ]
                musicServiceConnection.playMediaItem(
                    mediaItem = randomSongToPlay.mediaItem,
                    mediaItems = songsList.map { it.mediaItem },
                    shuffle = true
                )
            }
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
    val isLoadingPlayHistory: Boolean,
    val songsInPlayHistory: List<Song>,
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