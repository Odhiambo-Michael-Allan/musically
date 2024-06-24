package com.odesa.musicMatters.ui.forYou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.data.utils.sortSongs
import com.odesa.musicMatters.core.data.utils.subListNonStrict
import com.odesa.musicMatters.core.datatesting.albums.testAlbums
import com.odesa.musicMatters.core.datatesting.artists.testArtists
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Album
import com.odesa.musicMatters.core.model.Artist
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForYouScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val playlistRepository: PlaylistRepository,
    private val settingsRepository: SettingsRepository,
) : BaseViewModel(
    musicServiceConnection = musicServiceConnection,
    playlistRepository = playlistRepository,
    settingsRepository = settingsRepository
) {

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
            language = settingsRepository.language.value,
            themeMode = SettingsDefaults.themeMode,
            playlists = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeRecentlyAddedSongs() }
        viewModelScope.launch { observeSuggestedAlbums() }
        viewModelScope.launch { observeMostPlayedSongsMap() }
        viewModelScope.launch { observeSuggestedArtists() }
        viewModelScope.launch { observeRecentlyPlayedSongsPlaylist() }
        addOnPlaylistsChangeListener {
            _uiState.value = _uiState.value.copy( playlists = it )
        }
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

    private suspend fun observeLanguageChange() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
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

    private fun fetchRecentlyPlayedSongsUsing( playlist: Playlist) {
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

    fun playRecentlyAddedSong( song: Song ) {
        val recentlyAddedSongs = musicServiceConnection.cachedSongs.value.sortSongs(
            sortSongsBy = SortSongsBy.DATE_ADDED,
            reverse = true
        )
        playSongs(
            selectedSong = song,
            songsInPlaylist = recentlyAddedSongs
        )
    }

    fun playMostPlayedSong( song: Song ) {
        val mostPlayedSongsMap = playlistRepository.mostPlayedSongsMap.value
        val songs = musicServiceConnection.cachedSongs.value
        val sortedMap = mostPlayedSongsMap.toList().sortedByDescending { it.second }.toMap()
        val mostPlayedSongs = sortedMap.keys.mapNotNull { key ->
            songs.find { it.id == key }
        }
        playSongs(
            selectedSong = song,
            songsInPlaylist = mostPlayedSongs
        )
    }

    fun playSongInPlayHistory( song: Song ) {
        val playHistoryPlaylist = playlistRepository.recentlyPlayedSongsPlaylist.value
        val songs = musicServiceConnection.cachedSongs.value
        val songsInPlaylist = mutableListOf<Song>()
        playHistoryPlaylist.songIds.forEach { songId ->
            songs.find { it.id == songId }?.let {
                songsInPlaylist.add( it )
            }
        }
        playSongs(
            selectedSong = song,
            songsInPlaylist = songsInPlaylist
        )
    }

    fun shuffleAndPlay() {
        super.shuffleAndPlay(
            songs = musicServiceConnection.cachedSongs.value
        )
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
    val playlists: List<Playlist>,
)

internal val testForYouScreenUiState = ForYouScreenUiState(
    language = English,
    themeMode = SettingsDefaults.themeMode,
    isLoadingRecentSongs = false,
    recentlyAddedSongs = testSongs,
    isLoadingSuggestedAlbums = false,
    suggestedAlbums = testAlbums,
    isLoadingMostPlayedSongs = false,
    mostPlayedSongs = testSongs,
    isLoadingSuggestedArtists = false,
    suggestedArtists = testArtists,
    isLoadingRecentlyPlayedSongs = false,
    recentlyPlayedSongs = testSongs,
    playlists = emptyList()
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