package com.odesa.musically.ui.nowPlaying

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.LoopMode
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.artistTagSeparators
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.NOTHING_PLAYING
import com.odesa.musically.services.media.extensions.toSong
import com.odesa.musically.ui.components.PlaybackPosition
import com.odesa.musically.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class NowPlayingViewModel(
    application: Application,
    private val musicServiceConnection: MusicServiceConnection,
    settingsRepository: SettingsRepository,
) : AndroidViewModel( application ) {

    private val handler = Handler( Looper.getMainLooper() )
    private val _bottomSheetUiState = MutableStateFlow(
        NowPlayingBottomSheetUiState(
            currentlyPlayingSong = getCurrentlyPlayingSong(),
            currentlyPlayingSongIndex = 5,
            queueSize = 126,
            language = English,
            currentlyPlayingSongIsFavorite = true,
            controlsLayoutIsDefault = false,
            isPlaying = true,
            showSeekControls = true,
            showLyrics = false,
            playbackPosition = PlaybackPosition.zero,
            shuffle = true,
            currentLoopMode = LoopMode.Song,
            pauseOnCurrentSongEnd = false,
            currentPlayingSpeed = 2f,
            currentPlayingPitch = 2f,
            themeMode = settingsRepository.themeMode.value
        )
    )

    private val _bottomBarUiState = MutableStateFlow(
        NowPlayingBottomBarUiState(
            currentlyPlayingSong = getCurrentlyPlayingSong(),
            playbackPosition = PlaybackPosition.zero,
            textMarquee = settingsRepository.miniPlayerTextMarquee.value,
            showTrackControls = settingsRepository.miniPlayerShowTrackControls.value,
            showSeekControls = settingsRepository.miniPlayerShowSeekControls.value,
            isPlaying = musicServiceConnection.playbackState.value.isPlaying,
            themeMode = settingsRepository.themeMode.value
        )
    )
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()
    val bottomBarUiState = _bottomBarUiState.asStateFlow()

    private val _updatePlaybackPosition = MutableStateFlow( false )
    var updatePlaybackPosition = _updatePlaybackPosition.asStateFlow()

    init {
        viewModelScope.launch { observePlaybackState() }
        viewModelScope.launch { observeUpdatePlaybackPosition() }
    }

    private suspend fun observePlaybackState() {
        musicServiceConnection.playbackState.collect {
            if ( it.isPlaying ) {
                _updatePlaybackPosition.value = true
                updateBottomSheetPlaybackState( it.duration )
                updateBottomBarPlaybackState( it.duration )
            } else
                _updatePlaybackPosition.value = false
        }
    }

    private fun updateBottomSheetPlaybackState( duration: Long ) {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            playbackPosition = PlaybackPosition( 0L, duration )
        )
    }

    private fun updateBottomBarPlaybackState( duration: Long ) {
        _bottomBarUiState.value = _bottomBarUiState.value.copy(
            playbackPosition = PlaybackPosition( 0L, duration )
        )
    }

    private suspend fun observeUpdatePlaybackPosition() {
        viewModelScope.launch {
            updatePlaybackPosition.collect {
                if ( it ) checkPlaybackPosition( POSITION_UPDATE_INTERVAL_MILLIS )
            }
        }
    }

    /**
     * Internal function that recursively calls itself to check the current playback position and
     * updates the corresponding state value
     */
    private fun checkPlaybackPosition( delayMs: Long ): Boolean = handler.postDelayed( {
        val currentPosition = musicServiceConnection.player?.currentPosition ?: 0
        Timber.tag( "NOW-PLAYING-VIEW-MODEL" ).d( "CURRENT POSITION: $currentPosition" )
        if ( _bottomBarUiState.value.playbackPosition.played != currentPosition ) {
            updateBottomBarPlaybackState( currentPosition )
            updateBottomSheetPlaybackState( currentPosition )
        }
        if ( updatePlaybackPosition.value )
            checkPlaybackPosition( 1000 - ( currentPosition % 1000 ) )
    }, delayMs )

    fun onFavorite( songId: String ) {}

    fun playPause() {}

    fun playPreviousSong(): Boolean {
        return true
    }

    fun playNextSong(): Boolean {
        return true
    }

    fun fastRewind() {}

    fun fastForward() {}

    fun onSeekEnd( position: Long ) {}

    fun onArtworkClicked() {}

    fun toggleLoopMode() {}

    fun toggleShuffleMode() {}

    fun togglePauseOnCurrentSongEnd() {}

    fun onPlayingSpeedChange( playingSpeed: Float ) {}

    fun onPlayingPitchChange( playingPitch: Float ) {}

    override fun onCleared() {
        super.onCleared()
        _updatePlaybackPosition.value = false // Stop updating the position.
    }

    private fun getCurrentlyPlayingSong(): Song? {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        return if ( nowPlaying == NOTHING_PLAYING ) null
        else nowPlaying.toSong( artistTagSeparators )
    }

    class NowPlayingViewModelFactory(
        private val application: Application,
        private val musicServiceConnection: MusicServiceConnection,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress( "unchecked_cast" )
        override fun <T : ViewModel> create( modelClass: Class<T> ): T {
            return NowPlayingViewModel(
                application,
                musicServiceConnection,
                settingsRepository
            ) as T
        }
    }
}

data class NowPlayingBottomSheetUiState(
    val currentlyPlayingSong: Song?,
    val playbackPosition: PlaybackPosition,
    val currentlyPlayingSongIndex: Long,
    val queueSize: Int,
    val language: Language,
    val currentlyPlayingSongIsFavorite: Boolean,
    val controlsLayoutIsDefault: Boolean,
    val isPlaying: Boolean,
    val showSeekControls: Boolean,
    val showLyrics: Boolean,
    val shuffle: Boolean,
    val currentLoopMode: LoopMode,
    val pauseOnCurrentSongEnd: Boolean,
    val currentPlayingSpeed: Float,
    val currentPlayingPitch: Float,
    val themeMode: ThemeMode
)

data class NowPlayingBottomBarUiState(
    val currentlyPlayingSong: Song?,
    val playbackPosition: PlaybackPosition,
    val textMarquee: Boolean,
    val showTrackControls: Boolean,
    val showSeekControls: Boolean,
    val isPlaying: Boolean,
    val themeMode: ThemeMode
)

private const val POSITION_UPDATE_INTERVAL_MILLIS = 1L