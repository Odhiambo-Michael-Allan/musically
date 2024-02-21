package com.odesa.musically.ui.nowPlaying

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.media.LoopMode
import com.odesa.musically.services.media.Song
import com.odesa.musically.services.media.connection.MusicServiceConnection
import com.odesa.musically.services.media.connection.NOTHING_PLAYING
import com.odesa.musically.ui.components.PlaybackPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class NowPlayingViewModel(
    application: Application,
    private val musicServiceConnection: MusicServiceConnection
) : AndroidViewModel( application ) {

    private val currentMediaDuration = MutableStateFlow( 0L )
    private val currentMediaPosition = MutableStateFlow( 0L )
    private var updatePosition = true
    private val handler = Handler( Looper.getMainLooper() )
    private val _nowPlayingUiState = MutableStateFlow(
        NowPlayingUiState(
            currentMediaDuration = currentMediaDuration.value,
            currentMediaPosition = currentMediaPosition.value
        )
    )
    val nowPlayingUiState = _nowPlayingUiState.asStateFlow()

    init {
        viewModelScope.launch { observePlaybackState() }
//        viewModelScope.launch { observeNowPlaying() }
        checkPlaybackPosition( POSITION_UPDATE_INTERVAL_MILLIS )
    }

    private suspend fun observePlaybackState() {
        musicServiceConnection.playbackState.collect {
            _nowPlayingUiState.value = _nowPlayingUiState.value.copy(
                currentMediaDuration = it.duration
            )
        }
    }

    private suspend fun observeNowPlaying() {
        musicServiceConnection.nowPlaying.collect {
            updatePosition = it != NOTHING_PLAYING
        }
    }

    /**
     * Internal function that recursively calls itself to check the current playback position and
     * updates the corresponding state value
     */
    private fun checkPlaybackPosition( delayMs: Long ): Boolean = handler.postDelayed( {
        val currentPosition = musicServiceConnection.player?.currentPosition ?: 0
        Timber.tag( "MAIN-ACTIVITY-VIEW-MODEL" ).d( "CURRENT POSITION: $currentPosition" )
        if ( currentMediaPosition.value != currentPosition ) {
            _nowPlayingUiState.value = _nowPlayingUiState.value.copy(
                currentMediaPosition = currentPosition
            )
        }
        if ( updatePosition )
            checkPlaybackPosition( 1000 - ( currentPosition % 1000 ) )
    }, delayMs )

    fun onFavorite( songId: String ) {}

    fun playPause() {}

    fun playPreviousSong() {}

    fun playNextSong() {}

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
        updatePosition = false // Stop updating the position.
    }

    class MainActivityViewModelFactory(
        private val application: Application,
        private val musicServiceConnection: MusicServiceConnection
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress( "unchecked_cast" )
        override fun <T : ViewModel> create( modelClass: Class<T> ): T {
            return NowPlayingViewModel( application, musicServiceConnection ) as T
        }
    }
}

data class NowPlayingUiState(
    val currentMediaDuration: Long,
    val currentMediaPosition: Long,
    val currentlyPlayingSong: Song,
    val currentlyPlayingSongIndex: Long,
    val queueSize: Int,
    val language: Language,
    val currentlyPlayingSongIsFavorite: Boolean,
    val controlsLayoutIsDefault: Boolean,
    val isPlaying: Boolean,
    val enableSeekControls: Boolean,
    val showLyrics: Boolean,
    val fallbackResourceId: Int,
    val playbackPosition: PlaybackPosition,
    val shuffle: Boolean,
    val currentLoopMode: LoopMode,
    val pauseOnCurrentSongEnd: Boolean,
    val currentPlayingSpeed: Float,
    val currentPlayingPitch: Float,
)

private const val POSITION_UPDATE_INTERVAL_MILLIS = 1L