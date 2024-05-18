package com.odesa.musicMatters.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.testAlbums
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AlbumScreenViewModel(
    private val albumName: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AlbumScreenUiState(
            album = null,
            themeMode = settingsRepository.themeMode.value,
            isLoadingSongsInAlbum = true,
            language = settingsRepository.language.value,
            songsInAlbum = emptyList(),
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = emptyList(),
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
                isLoadingSongsInAlbum = isInitializing
            )
            if ( !isInitializing ) loadSongsInAlbum( albumName )
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
        it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id
    }

    fun addToFavorites( songId: String ) {
        viewModelScope.launch { playlistRepository.addToFavorites( songId ) }
    }

    private fun loadSongsInAlbum( albumName: String ) {
        val songs = musicServiceConnection.cachedSongs.value
        val albums = musicServiceConnection.cachedAlbums.value
        _uiState.value = _uiState.value.copy(
            album = albums.find { it.name == albumName },
            songsInAlbum = songs.filter { it.albumTitle == albumName },
        )
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
                mediaItems = uiState.value.songsInAlbum.map { it.mediaItem },
                shuffle = settingsRepository.shuffle.value
            )
        }
    }

    fun searchSongsMatching( query: String ) = musicServiceConnection.searchSongsMatching( query )
    fun createPlaylist( playlistName: String, songs: List<Song> ) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    title = playlistName,
                    songIds = songs.map { it.id }
                )
            )
        }
    }

}

@Suppress( "UNCHECKED_CAST" )
class AlbumScreenViewModelFactory(
    private val albumName: String,
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( AlbumScreenViewModel(
            albumName = albumName,
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        ) as T )
}

data class AlbumScreenUiState(
    val album: Album?,
    val themeMode: ThemeMode,
    val isLoadingSongsInAlbum: Boolean,
    val language: Language,
    val songsInAlbum: List<Song>,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val playlists: List<Playlist>,
)

val testAlbumScreenUiState = AlbumScreenUiState(
    album = testAlbums.first(),
    themeMode = SettingsDefaults.themeMode,
    isLoadingSongsInAlbum = false,
    language = SettingsDefaults.language,
    songsInAlbum = testSongs,
    currentlyPlayingSongId = testSongs.last().id,
    favoriteSongIds = emptyList(),
    playlists = emptyList(),
)