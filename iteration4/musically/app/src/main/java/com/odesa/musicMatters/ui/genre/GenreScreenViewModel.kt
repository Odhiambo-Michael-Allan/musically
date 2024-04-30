package com.odesa.musicMatters.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSIC_MATTERS_TRACKS_ROOT
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenreScreenViewModel(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GenreScreenUistate(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList(),
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = emptyList(),
            isLoading = true
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeModeChange() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeFavoriteSongIds() }
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

    fun addToFavorites( songId: String ) {
        viewModelScope.launch {
            if ( playlistRepository.isFavorite( songId ) )
                playlistRepository.removeFromFavorites( songId )
            else
                playlistRepository.addToFavorites( songId )
        }
    }

    fun loadSongsWithGenre( genre: String ) {
        musicServiceConnection.runWhenInitialized {
            viewModelScope.launch {
                val songs = musicServiceConnection.getChildren( MUSIC_MATTERS_TRACKS_ROOT ).map {
                    it.toSong( artistTagSeparators )
                }
                _uiState.value = _uiState.value.copy(
                    songs = songs.filter { it.genre?.lowercase() == genre.lowercase() },
                    isLoading = false
                )
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
}

@Suppress( "UNCHECKED_CAST" )
class GenreScreenViewModelFactory(
    private val musicServiceConnection: MusicServiceConnection,
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create( modelClass: Class<T> ) =
        ( GenreScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        ) as T )
}

data class GenreScreenUistate(
    val language: Language,
    val themeMode: ThemeMode,
    val songs: List<Song>,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: List<String>,
    val isLoading: Boolean
)

val testGenreScreenUiState = GenreScreenUistate(
    language = SettingsDefaults.language,
    themeMode = SettingsDefaults.themeMode,
    songs = testSongs,
    currentlyPlayingSongId = testSongs.first().id,
    favoriteSongIds = emptyList(),
    isLoading = false
)