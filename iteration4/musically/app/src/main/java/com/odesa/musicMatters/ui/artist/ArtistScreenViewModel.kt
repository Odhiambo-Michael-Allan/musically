package com.odesa.musicMatters.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Album
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toAlbum
import com.odesa.musicMatters.services.media.extensions.toArtist
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_ALBUMS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_ARTISTS_ROOT
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ArtistScreenUiState(
            artist = null,
            songsByArtist = emptyList(),
            isLoadingSongsByArtist = true,
            albumsByArtist = emptyList(),
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = playlistRepository.favoritesPlaylist.value.songIds,
            playlists = fetchRequiredPlaylistsFrom( playlistRepository.playlists.value )
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
        viewModelScope.launch { observeCurrentlyPlayingSongId() }
        viewModelScope.launch { observeFavoritesPlaylist() }
        viewModelScope.launch { observePlaylists() }
    }

    private fun fetchRequiredPlaylistsFrom( playlists: List<Playlist> ) = playlists.filter {
        it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id
    }

    fun loadSongsBy( artistName: String ) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val artist = musicServiceConnection.getChildren( MUSIC_MATTERS_ARTISTS_ROOT ).map {
                    it.toArtist()
                }.find { it.name == artistName }
                val songsByArtist = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT ).map {
                    it.toSong( artistTagSeparators )
                }.filter { it.artists.contains( artistName ) }
                val albumsByArtist = musicServiceConnection.getChildren( MUSIC_MATTERS_ALBUMS_ROOT ).map {
                    it.toAlbum()
                }.filter { it.artists.contains( artistName ) }
                _uiState.value = _uiState.value.copy(
                    artist = artist,
                    songsByArtist = songsByArtist,
                    albumsByArtist = albumsByArtist,
                    isLoadingSongsByArtist = false
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

    private suspend fun observeThemeModeChange() {
        settingsRepository.themeMode.collect {
            _uiState.value = _uiState.value.copy(
                themeMode = it
            )
        }
    }

    private suspend fun observeCurrentlyPlayingSongId() {
        musicServiceConnection.nowPlaying.collect {
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSongId = it.mediaId
            )
        }
    }

    private suspend fun observeFavoritesPlaylist() {
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

    fun addToFavorites( songId: String ) {
        viewModelScope.launch {
            if ( playlistRepository.isFavorite( songId ) )
                playlistRepository.removeFromFavorites( songId )
            else
                playlistRepository.addToFavorites( songId )
        }
    }

    fun playSong( mediaItem: MediaItem ) {
        viewModelScope.launch {
            val mediaItems = uiState.value.songsByArtist.map { it.mediaItem }
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = mediaItems,
                shuffle = settingsRepository.shuffle.value
            )
        }
    }

    fun addSongToPlaylist( playlist: Playlist, song: Song ) {
        viewModelScope.launch {
            playlistRepository.addSongIdToPlaylist( song.id, playlist.id )
        }
    }

    fun playAlbum( albumName: String ) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT )
                val songsInAlbum = songs.filter { it.mediaMetadata.albumTitle == albumName }
                musicServiceConnection.playMediaItem(
                    mediaItem = songsInAlbum.first(),
                    mediaItems = songsInAlbum,
                    shuffle = settingsRepository.shuffle.value
                )
            }
        }
    }
}

@Suppress( "UNCHECKED_CAST" )
class ArtistScreenViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( ArtistScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        ) as T )
}

data class ArtistScreenUiState(
    val artist: Artist?,
    val songsByArtist: List<Song>,
    val isLoadingSongsByArtist: Boolean,
    val albumsByArtist: List<Album>,
    val language: Language,
    val themeMode: ThemeMode,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val playlists: List<Playlist>,
)