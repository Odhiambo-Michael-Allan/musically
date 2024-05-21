package com.odesa.musicMatters.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.Playlist
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.SortSongsBy
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SongsScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList(),
            sortSongsBy = settingsRepository.sortSongsBy.value,
            sortSongsInReverse = settingsRepository.sortSongsInReverse.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = playlistRepository.favoritesPlaylist.value.songIds,
            isLoading = musicServiceConnection.isInitializing.value,
            playlists = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeSongs() }
        viewModelScope.launch { observeMusicServiceConnectionInitializedStatus() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeFavoriteSongIds() }
        viewModelScope.launch { observePlaylists() }
        viewModelScope.launch { observeSortSongsBy() }
        viewModelScope.launch { observeSortSongsInReverse() }
    }

    private suspend fun observeSongs() {
        musicServiceConnection.cachedSongs.collect {
            _uiState.value = _uiState.value.copy(
                songs = sortSongs( it, settingsRepository.sortSongsBy.value, settingsRepository.sortSongsInReverse.value )
            )
        }
    }

    private suspend fun observeMusicServiceConnectionInitializedStatus() {
        musicServiceConnection.isInitializing.collect {
            _uiState.value = _uiState.value.copy(
                isLoading = it
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

    private suspend fun observeThemeMode() {
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

    private suspend fun observeSortSongsBy() {
        settingsRepository.sortSongsBy.collect {
            _uiState.value = _uiState.value.copy(
                sortSongsBy = it,
                songs = sortSongs( musicServiceConnection.cachedSongs.value, it, settingsRepository.sortSongsInReverse.value )
            )
        }
    }

    private suspend fun observeSortSongsInReverse() {
        settingsRepository.sortSongsInReverse.collect {
            _uiState.value = _uiState.value.copy(
                sortSongsInReverse = it,
                songs = sortSongs( musicServiceConnection.cachedSongs.value, settingsRepository.sortSongsBy.value, it )
            )
        }
    }

    private fun fetchRequiredPlaylistsFrom( playlists: List<Playlist> ): List<Playlist> {
        val requiredPlaylists = playlists.filter {
            it != playlistRepository.recentlyPlayedSongsPlaylist.value &&
                    it != playlistRepository.mostPlayedSongsPlaylist.value
        }
        return requiredPlaylists
    }

    fun addToFavorites( songId: String ) {
        viewModelScope.launch { playlistRepository.addToFavorites( songId ) }
    }

    fun addSongToPlaylist( playlist: Playlist, songs: List<Song> ) {
        viewModelScope.launch {
            songs.forEach {
                playlistRepository.addSongIdToPlaylist( it.id, playlist.id )
            }
        }
    }

    fun playMedia(
        mediaItem: MediaItem,
    ) {
        viewModelScope.launch {
            musicServiceConnection.playMediaItem(
                mediaItem = mediaItem,
                mediaItems = uiState.value.songs.map { it.mediaItem },
                shuffle = settingsRepository.shuffle.value
            )
        }
    }

    fun shuffleAndPlay() {
        viewModelScope.launch {
            musicServiceConnection.shuffleAndPlay(
                mediaItems = uiState.value.songs.map { it.mediaItem }
            )
        }
    }

    fun searchSongsMatching( query: String ): List<Song> {
        return musicServiceConnection.searchSongsMatching( query )
    }

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

    fun setSortSongsBy( sortSongsBy: SortSongsBy ) {
        viewModelScope.launch {
            settingsRepository.setCurrentSortSongsByValueTo( sortSongsBy )
        }
    }

    fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        viewModelScope.launch {
            settingsRepository.setSortSongsInReverse( sortSongsInReverse )
        }
    }

    private fun sortSongs( songs: List<Song>, sortSongsBy: SortSongsBy, reverse: Boolean ): List<Song> {
        return when ( sortSongsBy ) {
            SortSongsBy.TITLE -> if ( reverse ) songs.sortedByDescending { it.title } else songs.sortedBy { it.title }
            SortSongsBy.ALBUM -> if ( reverse ) songs.sortedByDescending { it.albumTitle } else songs.sortedBy { it.albumTitle }
            SortSongsBy.ARTIST -> if ( reverse ) songs.sortedByDescending { it.artists.joinToString() } else songs.sortedBy { it.artists.joinToString() }
            SortSongsBy.COMPOSER -> if ( reverse ) songs.sortedByDescending { it.composer } else songs.sortedBy { it.composer }
            SortSongsBy.DURATION -> if ( reverse ) songs.sortedByDescending { it.duration } else songs.sortedBy { it.duration }
            SortSongsBy.YEAR -> if ( reverse ) songs.sortedByDescending { it.year } else songs.sortedBy { it.year }
            SortSongsBy.DATE_ADDED -> if ( reverse ) songs.sortedByDescending { it.dateModified } else songs.sortedBy { it.dateModified }
            SortSongsBy.FILENAME -> if ( reverse ) songs.sortedByDescending { it.path } else songs.sortedBy { it.path }
            SortSongsBy.TRACK_NUMBER -> if ( reverse ) songs.sortedByDescending { it.trackNumber } else songs.sortedBy { it.trackNumber }
            SortSongsBy.CUSTOM -> songs.shuffled()
        }
    }
}

data class SongsScreenUiState(
    val language: Language,
    val themeMode: ThemeMode,
    val songs: List<Song>,
    val sortSongsBy: SortSongsBy,
    val sortSongsInReverse: Boolean,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val isLoading: Boolean,
    val playlists: List<Playlist>,
)

@Suppress( "UNCHECKED_CAST" )
class SongsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SongsScreenViewModel(
            settingsRepository,
            playlistRepository,
            musicServiceConnection,
        ) as T )
}

const val SongsViewModelTag = "SONGS-VIEW-MODEL"