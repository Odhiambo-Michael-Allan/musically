package com.odesa.musicMatters.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class GenreScreenViewModel(
    private val genreName: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GenreScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songsInGenre = emptyList(),
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = emptyList(),
            isLoading = musicServiceConnection.isInitializing.value,
            playlists = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeFavoriteSongIds() }
        viewModelScope.launch { observePlaylists() }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect { isInitializing ->
            _uiState.value = _uiState.value.copy(
                isLoading = isInitializing
            )
            if ( !isInitializing ) loadSongsWithGenre( genreName )
        }
    }

    private fun loadSongsWithGenre( genre: String ) {
        val songs = musicServiceConnection.cachedSongs.value
        _uiState.value = _uiState.value.copy(
            songsInGenre = songs.filter { it.genre?.lowercase() == genre.lowercase() },
        )
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

    private suspend fun observePlaylists() {
        playlistRepository.playlists.collect {
            _uiState.value = _uiState.value.copy(
                playlists = fetchRequiredPlaylistsFrom( it )
            )
        }
    }

    private fun fetchRequiredPlaylistsFrom( playlists: List<Playlist> ) = playlists.filter {
        it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.mostPlayedSongsPlaylist.value.id
    }

    fun addToFavorites( songId: String ) {
        viewModelScope.launch { playlistRepository.addToFavorites( songId ) }
    }

    fun addSongToPlaylist( playlist: Playlist, song: Song ) {
        viewModelScope.launch {
            playlistRepository.addSongIdToPlaylist( song.id, playlist.id )
        }
    }

    fun playMedia(
        mediaItem: MediaItem,
    ) {
        viewModelScope.launch {
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = uiState.value.songsInGenre.map { it.mediaItem },
                shuffle = settingsRepository.shuffle.value
            )
        }
    }

    fun searchSongsMatching( query: String ) = musicServiceConnection.searchSongsMatching( query )
    fun createPlaylist( title: String, songs: List<Song> ) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    songIds = songs.map { it.id }
                )
            )
        }
    }
}

@Suppress( "UNCHECKED_CAST" )
class GenreScreenViewModelFactory(
    private val genreName: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( GenreScreenViewModel(
            genreName = genreName,
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        ) as T )
}

data class GenreScreenUiState(
    val language: Language,
    val themeMode: ThemeMode,
    val songsInGenre: List<Song>,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val isLoading: Boolean,
    val playlists: List<Playlist>,
)

val testGenreScreenUiState = GenreScreenUiState(
    language = SettingsDefaults.language,
    themeMode = SettingsDefaults.themeMode,
    songsInGenre = testSongs,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = emptyList(),
    isLoading = false,
    playlists = emptyList(),
)