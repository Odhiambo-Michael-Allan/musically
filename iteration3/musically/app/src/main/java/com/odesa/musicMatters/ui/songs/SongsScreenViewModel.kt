package com.odesa.musicMatters.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.media.Song
import com.odesa.musicMatters.services.media.artistTagSeparators
import com.odesa.musicMatters.services.media.connection.MusicServiceConnection
import com.odesa.musicMatters.services.media.connection.TAG
import com.odesa.musicMatters.services.media.extensions.isEnded
import com.odesa.musicMatters.services.media.extensions.isPlayEnabled
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.services.media.library.MUSICALLY_TRACKS_ROOT
import com.odesa.musicMatters.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SongsScreenViewModel(
    private val settingsRepository: SettingsRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private var playlist = emptyList<MediaItem>()

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList(),
            currentlyPlayingSongId = musicServiceConnection.nowPlaying.value.mediaId,
            favoriteSongIds = emptySet(),
            isLoading = true
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { fetchMediaItems() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeCurrentlyPlayingSong() }
        viewModelScope.launch { observeFavoriteSongIds() }
    }

    private suspend fun fetchMediaItems() {
        playlist = musicServiceConnection.getChildren( MUSICALLY_TRACKS_ROOT )
        _uiState.value = _uiState.value.copy(
            songs = playlist.map { it.toSong( artistTagSeparators ) }
        )
        _uiState.value = _uiState.value.copy (
            isLoading = false
        )
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

    fun addToFavorites( songId: String ) {
        viewModelScope.launch {
            if ( playlistRepository.isFavorite( songId ) )
                playlistRepository.removeFromFavorites( songId )
            else
                playlistRepository.addToFavorites( songId )
        }
    }

    fun playMedia(
        mediaItem: MediaItem,
        pauseThenPlaying: Boolean,
    ) {
        val player = musicServiceConnection.player ?: return
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val isPrepared = player.playbackState != Player.STATE_IDLE
        if ( isPrepared && mediaItem.mediaId == nowPlaying.mediaId ) {
            when {
                player.isPlaying -> if ( pauseThenPlaying ) player.pause() else Unit
                player.isPlayEnabled -> player.play()
                player.isEnded -> player.seekTo( C.TIME_UNSET )
                else -> {
                    Timber.tag( TAG ).d( "Playable item clicked but neither play nor pause " +
                            "are enabled! ( mediaId = ${mediaItem.mediaId} )" )
                }
            }
        } else {
            viewModelScope.launch {
                val indexOfSelectedMediaItem = playlist.indexOf( mediaItem )
                Timber.tag( TAG ).d( "INDEX OF MEDIA ITEM TO PLAY: $indexOfSelectedMediaItem" )
                player.setMediaItems( playlist, indexOfSelectedMediaItem, C.TIME_UNSET )
                player.prepare()
                player.play()
            }
        }
    }
}

data class SongsScreenUiState(
    val language: Language,
    val themeMode: ThemeMode,
    val songs: List<Song>,
    val currentlyPlayingSongId: String,
    val favoriteSongIds: Set<String>,
    val isLoading: Boolean
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