package com.odesa.musicMatters.ui.forYou

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
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.theme.ThemeMode
import com.odesa.musicMatters.utils.subListNonStrict
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

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
            language = settingsRepository.language.value,
            themeMode = SettingsDefaults.themeMode,
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

    fun playSongsInAlbum( album: Album, shuffle: Boolean ) {
        viewModelScope.launch {
            val songsInAlbum = musicServiceConnection.cachedSongs.value.filter {
                it.albumTitle == album.name
            }.map { it.mediaItem }
            musicServiceConnection.playMediaItem(
                mediaItem = if ( shuffle ) songsInAlbum.random() else songsInAlbum.first(),
                mediaItems = songsInAlbum,
                shuffle = shuffle
            )
        }
    }

    fun addSongsInAlbumToQueue( album: Album ): Int {
        val songsInAlbum = musicServiceConnection.cachedSongs.value.filter {
            it.albumTitle == album.name
        }.map { it.mediaItem }
        val songsInAlbumAlreadyInQueue = songsInAlbum.size.minus(
            musicServiceConnection.mediaItemsInQueue.value.count { songsInAlbum.contains( it ) } )
        songsInAlbum.forEach {
            musicServiceConnection.addToQueue( it )
        }
        return songsInAlbumAlreadyInQueue
    }

    fun playSongsInAlbumNext( album: Album ) {
        val songsInAlbum = musicServiceConnection.cachedSongs.value.filter {
            it.albumTitle == album.name
        }.map { it.mediaItem }
        songsInAlbum.forEach {
            musicServiceConnection.playNext( it )
        }
    }

    fun searchSongsMatching( query: String ) =
        musicServiceConnection.searchSongsMatching( query )

    fun getPlaylists() = playlistRepository.playlists.value.filter {
        it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id
    }

    fun getSongsInAlbum( album: Album ) =
        musicServiceConnection.cachedSongs.value.filter { it.albumTitle == album.name }

    fun createPlaylist( playlistName: String, songsToAddToPlaylist: List<Song> ) {
        viewModelScope.launch {
            playlistRepository.savePlaylist(
                Playlist(
                    id = UUID.randomUUID().toString(),
                    title = playlistName,
                    songIds = songsToAddToPlaylist.map { it.id }
                )
            )
        }
    }

    fun addSongsToPlaylist( playlist: Playlist, songs: List<Song> ) {
        viewModelScope.launch {
            songs.forEach {
                playlistRepository.addSongIdToPlaylist( it.id, playlist.id )
            }
        }
    }

    fun getSongsInPlaylist( playlist: Playlist ) =
        musicServiceConnection.cachedSongs.value.filter { playlist.songIds.contains( it.id ) }

    fun getSongsByArtist( artist: Artist ) =
        musicServiceConnection.cachedSongs.value.filter { it.artists.contains( artist.name ) }

    fun playSongsByArtistNext( artist: Artist ) {
        musicServiceConnection.cachedSongs.value
            .filter { it.artists.contains( artist.name ) }
            .map { it.mediaItem }
            .forEach {
                musicServiceConnection.playNext( it )
            }
    }

    fun playSongsByArtist( artist: Artist, shuffle: Boolean = false ) {
        viewModelScope.launch {
            val songsByArtist = musicServiceConnection.cachedSongs.value
                .filter { it.artists.contains( artist.name ) }
                .map { it.mediaItem }
            musicServiceConnection.playMediaItem(
                mediaItem = if ( shuffle ) songsByArtist.random() else songsByArtist.first(),
                mediaItems = songsByArtist,
                shuffle = shuffle
            )
        }
    }

    fun addSongsByArtistToQueue( artist: Artist ): Int {
        val songsByArtist = musicServiceConnection.cachedSongs.value
            .filter { it.artists.contains( artist.name ) }
            .map { it.mediaItem }
        val numberOfSongsByArtistAlreadyInQueue =
            songsByArtist.size.minus(
                musicServiceConnection.mediaItemsInQueue.value.count { songsByArtist.contains( it ) }
            )
        songsByArtist.forEach {
            musicServiceConnection.addToQueue( it )
        }
        return numberOfSongsByArtistAlreadyInQueue
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