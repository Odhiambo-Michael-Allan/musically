package com.odesa.musicMatters.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musicMatters.core.common.connection.MusicServiceConnection
import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.SortSongsBy
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.data.utils.sortSongs
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.model.Playlist
import com.odesa.musicMatters.core.model.Song
import com.odesa.musicMatters.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongsScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : BaseViewModel(
    musicServiceConnection = musicServiceConnection,
    settingsRepository = settingsRepository,
    playlistRepository = playlistRepository,
) {

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList(),
            sortSongsBy = settingsRepository.sortSongsBy.value,
            sortSongsInReverse = settingsRepository.sortSongsInReverse.value,
            currentlyPlayingSongId = musicServiceConnection.nowPlayingMediaItem.value.mediaId,
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
        viewModelScope.launch { observeSortSongsBy() }
        viewModelScope.launch { observeSortSongsInReverse() }
        addOnPlaylistsChangeListener {
            _uiState.value = _uiState.value.copy( playlists = it )
        }
    }

    private suspend fun observeSongs() {
        musicServiceConnection.cachedSongs.collect {
            _uiState.value = _uiState.value.copy(
                songs = it.sortSongs( settingsRepository.sortSongsBy.value, settingsRepository.sortSongsInReverse.value )
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
        musicServiceConnection.nowPlayingMediaItem.collect {
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

    private suspend fun observeSortSongsBy() {
        settingsRepository.sortSongsBy.collect {
            _uiState.value = _uiState.value.copy(
                sortSongsBy = it,
                songs = musicServiceConnection.cachedSongs.value.sortSongs( it, settingsRepository.sortSongsInReverse.value )
            )
        }
    }

    private suspend fun observeSortSongsInReverse() {
        settingsRepository.sortSongsInReverse.collect {
            _uiState.value = _uiState.value.copy(
                sortSongsInReverse = it,
                songs = musicServiceConnection.cachedSongs.value.sortSongs( settingsRepository.sortSongsBy.value, it )
            )
        }
    }

    fun setSortSongsBy( sortSongsBy: SortSongsBy) {
        viewModelScope.launch {
            settingsRepository.setSortSongsBy( sortSongsBy )
        }
    }

    fun setSortSongsInReverse( sortSongsInReverse: Boolean ) {
        viewModelScope.launch {
            settingsRepository.setSortSongsInReverse( sortSongsInReverse )
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
    val playlists: List<Playlist>
)

internal val testSongsScreenUiState = SongsScreenUiState(
    language = English,
    songs = testSongs,
    themeMode = ThemeMode.LIGHT,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = testSongs.map { it.id },
    isLoading = true,
    sortSongsBy = SortSongsBy.TITLE,
    sortSongsInReverse = false,
    playlists = emptyList()
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