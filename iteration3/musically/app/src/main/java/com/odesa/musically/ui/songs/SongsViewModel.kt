package com.odesa.musically.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.TAG
import com.odesa.musically.services.media.extensions.isEnded
import com.odesa.musically.services.media.extensions.isPlayEnabled
import com.odesa.musically.services.media.extensions.stringRep
import com.odesa.musically.services.media.extensions.toSong
import com.odesa.musically.services.media.library.MUSICALLY_TRACKS_ROOT
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SongsViewModel(
    private val settingsRepository: SettingsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SongsScreenUiState(
            language = settingsRepository.language.value,
            themeMode = settingsRepository.themeMode.value,
            songs = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { fetchMediaItems() }
        viewModelScope.launch { observeLanguageChange() }
        viewModelScope.launch { observeThemeMode() }
    }

    private suspend fun fetchMediaItems() {
        val mediaItemList = musicServiceConnection.getChildren( MUSICALLY_TRACKS_ROOT )
        mediaItemList.forEach {
            Timber.tag( SongsViewModelTag ).d( it.stringRep() )
        }
        _uiState.value = _uiState.value.copy(
            songs = mediaItemList.map { it.toSong() }
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

    fun playMedia(
        mediaItem: MediaItem,
        pauseThenPlaying: Boolean,
        parentMediaId: String?
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
                var playlist: MutableList<MediaItem> = arrayListOf()
                // Load the children of the parent if requested.
                parentMediaId?.let {
                    playlist = musicServiceConnection.getChildren( parentMediaId ).let { children ->
                        children.filter {
                            it.mediaMetadata.isPlayable ?: false
                        }
                    }.toMutableList()
                }
                if ( playlist.isEmpty() ) {
                    playlist.add( mediaItem )
                }
                val indexOf = playlist.indexOf( mediaItem )
                val startWindowIndex = if ( indexOf >= 0 ) indexOf else 0
                player.setMediaItems( playlist, startWindowIndex, /* startPositionMs*/ C.TIME_UNSET )
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
)

@Suppress( "UNCHECKED_CAST" )
class SongsViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create( modelClass: Class<T> ) =
        ( SongsViewModel(
            settingsRepository,
            musicServiceConnection,
        ) as T )
}

const val SongsViewModelTag = "SONGS-VIEW-MODEL"