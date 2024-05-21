package com.odesa.musicMatters.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.SortArtistsBy
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ArtistsScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ArtistsScreenUiState(
            artists = emptyList(),
            isLoadingArtists = true,
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            sortArtistsBy = settingsRepository.sortArtistsBy.value,
            sortArtistsInReverse = settingsRepository.sortArtistsInReverse.value
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeArtists() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
        viewModelScope.launch { observeSortArtistsBy() }
        viewModelScope.launch { observeSortArtistsInReverse() }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isLoadingArtists = it
            )
        }
    }

    private suspend fun observeArtists() {
        musicServiceConnection.cachedArtists.collect {
            _uiState.value = _uiState.value.copy(
                artists = sortArtists(
                    it,
                    settingsRepository.sortArtistsBy.value,
                    settingsRepository.sortArtistsInReverse.value
                )
            )
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

    private suspend fun observeSortArtistsBy() {
        settingsRepository.sortArtistsBy.collect {
            _uiState.value = _uiState.value.copy(
                sortArtistsBy = it,
                artists = sortArtists(
                    artists = musicServiceConnection.cachedArtists.value,
                    sortArtistsBy = it,
                    reverse = settingsRepository.sortArtistsInReverse.value
                )
            )
        }
    }

    private suspend fun observeSortArtistsInReverse() {
        settingsRepository.sortArtistsInReverse.collect {
            _uiState.value = _uiState.value.copy(
                sortArtistsInReverse = it,
                artists = sortArtists(
                    artists = musicServiceConnection.cachedArtists.value,
                    sortArtistsBy = settingsRepository.sortArtistsBy.value,
                    reverse = it
                )
            )
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

    fun playSongsByArtistNext( artist: Artist ) {
        musicServiceConnection.cachedSongs.value
            .filter { it.artists.contains( artist.name ) }
            .map { it.mediaItem }
            .forEach {
                musicServiceConnection.playNext( it )
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

    fun addSongsToPlaylist( playlist: Playlist, songs: List<Song> ) {
        viewModelScope.launch {
            songs.forEach {
                playlistRepository.addSongIdToPlaylist( it.id, playlist.id )
            }
        }
    }

    fun getSongsByArtist( artist: Artist ) =
        musicServiceConnection.cachedSongs.value.filter { it.artists.contains( artist.name ) }

    fun getPlaylists() = playlistRepository.playlists.value.filter {
        it.id != playlistRepository.mostPlayedSongsPlaylist.value.id &&
                it.id != playlistRepository.recentlyPlayedSongsPlaylist.value.id
    }

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

    fun searchSongsMatching( query: String ) =
        musicServiceConnection.searchSongsMatching( query )

    fun getSongsInPlaylist( playlist: Playlist ) =
        musicServiceConnection.cachedSongs.value.filter { playlist.songIds.contains( it.id ) }

    fun setSortArtistsBy( sortType: SortArtistsBy ) {
        viewModelScope.launch {
            settingsRepository.setSortArtistsBy( sortType )
        }
    }

    fun setSortArtistsInReverse( sortReverse: Boolean ) {
        viewModelScope.launch {
            settingsRepository.setSortArtistsInReverseTo( sortReverse )
        }
    }

    private fun sortArtists( artists: List<Artist>, sortArtistsBy: SortArtistsBy, reverse: Boolean ): List<Artist> {
        return when ( sortArtistsBy ) {
            SortArtistsBy.ARTIST_NAME -> if ( reverse ) artists.sortedByDescending { it.name } else artists.sortedBy { it.name }
            SortArtistsBy.ALBUMS_COUNT -> if ( reverse ) artists.sortedByDescending { it.albumCount } else artists.sortedBy { it.albumCount }
            SortArtistsBy.TRACKS_COUNT -> if ( reverse ) artists.sortedByDescending { it.trackCount } else artists.sortedBy { it.trackCount }
            else -> artists.shuffled()
        }
    }
}

data class ArtistsScreenUiState(
    val artists: List<Artist>,
    val isLoadingArtists: Boolean,
    val language: Language,
    val themeMode: ThemeMode,
    val sortArtistsBy: SortArtistsBy,
    val sortArtistsInReverse: Boolean,
)


@Suppress( "UNCHECKED_CAST" )
class ArtistsViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( ArtistsScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        ) as T )
}