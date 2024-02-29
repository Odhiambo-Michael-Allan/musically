package com.odesa.musically.ui.nowPlaying

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.storage.preferences.impl.LoopMode
import com.odesa.musically.data.storage.preferences.impl.toRepeatMode
import com.odesa.musically.services.i18n.Language
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
    private val settingsRepository: SettingsRepository,
) : AndroidViewModel( application ) {

    private val handler = Handler( Looper.getMainLooper() )
    private val _uiState = MutableStateFlow(
        NowPlayingScreenUiState(
            currentlyPlayingSong = getCurrentlyPlayingSong(),
            currentlyPlayingSongIndex = musicServiceConnection.currentlyPlayingMediaItemIndex.value,
            playbackPosition = PlaybackPosition.zero,
            queueSize = musicServiceConnection.queueSize.value,
            language = settingsRepository.language.value,
            currentlyPlayingSongIsFavorite = true,
            controlsLayoutIsDefault = settingsRepository.controlsLayoutIsDefault.value,
            isPlaying = musicServiceConnection.isPlaying.value,
            showTrackControls = settingsRepository.miniPlayerShowTrackControls.value,
            showSeekControls = settingsRepository.miniPlayerShowSeekControls.value,
            showLyrics = settingsRepository.showLyrics.value,
            shuffle = settingsRepository.shuffle.value,
            currentLoopMode = settingsRepository.currentLoopMode.value,
            pauseOnCurrentSongEnd = settingsRepository.pauseOnCurrentSongEnd.value,
            currentPlayingSpeed = settingsRepository.currentPlayingSpeed.value,
            currentPlayingPitch = settingsRepository.currentPlayingPitch.value,
            themeMode = settingsRepository.themeMode.value,
            textMarquee = settingsRepository.miniPlayerTextMarquee.value,
        )
    )

    val uiState = _uiState.asStateFlow()

    private val _updatePlaybackPosition = MutableStateFlow( false )
    var updatePlaybackPosition = _updatePlaybackPosition.asStateFlow()

    init {
        viewModelScope.launch { observeNowPlaying() }
        viewModelScope.launch { observePlaybackState() }
        viewModelScope.launch { observeUpdatePlaybackPosition() }
        viewModelScope.launch { observeQueueSize() }
        viewModelScope.launch { observeCurrentlyPlayingMediaItemIndex() }
        viewModelScope.launch { observeLanguage() }
        viewModelScope.launch { observeIsPlaying() }
        viewModelScope.launch { observeThemeMode() }
        viewModelScope.launch { observeTextMarquee() }
        viewModelScope.launch { observeShowTrackControls() }
        viewModelScope.launch { observeShowSeekControls() }
        viewModelScope.launch { observeControlsLayoutIsDefault() }
        viewModelScope.launch { observeShowLyrics() }
        viewModelScope.launch { observeShuffle() }
        viewModelScope.launch { observeLoopMode() }
        viewModelScope.launch { observePauseOnCurrentSongEnd() }
        viewModelScope.launch { observeCurrentPlayingSpeed() }
        viewModelScope.launch { observeCurrentPlayingPitch() }
    }

    private suspend fun observeNowPlaying() {
        musicServiceConnection.nowPlaying.collect {
            val song = getCurrentlyPlayingSong()
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSong = song
            )
        }
    }

    private suspend fun observePlaybackState() {
        musicServiceConnection.playbackState.collect {
            if ( it.isPlaying ) {
                _updatePlaybackPosition.value = true
                updateBottomSheetPlaybackState( 0L, it.duration )
                updatePlaybackState( 0L, it.duration )
            } else
                _updatePlaybackPosition.value = false
        }
    }

    private fun updateBottomSheetPlaybackState( played: Long, total: Long ) {
        _uiState.value = _uiState.value.copy(
            playbackPosition = PlaybackPosition( played, total )
        )
    }

    private fun updatePlaybackState(played: Long, total: Long ) {
        _uiState.value = _uiState.value.copy(
            playbackPosition = PlaybackPosition( played, total )
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
        if ( _uiState.value.playbackPosition.played != currentPosition ) {
            updatePlaybackState( currentPosition,
                _uiState.value.playbackPosition.total )
        }
        if ( updatePlaybackPosition.value )
            checkPlaybackPosition( 1000 - ( currentPosition % 1000 ) )
    }, delayMs )

    private suspend fun observeQueueSize() {
        musicServiceConnection.queueSize.collect {
            _uiState.value = _uiState.value.copy(
                queueSize = it
            )
        }
    }

    private suspend fun observeCurrentlyPlayingMediaItemIndex() {
        musicServiceConnection.currentlyPlayingMediaItemIndex.collect {
            _uiState.value = _uiState.value.copy(
                currentlyPlayingSongIndex = it
            )
        }
    }

    private suspend fun observeLanguage() {
        settingsRepository.language.collect {
            _uiState.value = _uiState.value.copy(
                language = it
            )
        }
    }

    private suspend fun observeIsPlaying() {
        musicServiceConnection.isPlaying.collect {
            _uiState.value = _uiState.value.copy(
                isPlaying = it
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

    private suspend fun observeTextMarquee() {
        settingsRepository.miniPlayerTextMarquee.collect {
            _uiState.value = _uiState.value.copy(
                textMarquee = it
            )
        }
    }

    private suspend fun observeShowTrackControls() {
        settingsRepository.miniPlayerShowTrackControls.collect {
            _uiState.value = _uiState.value.copy(
                showTrackControls = it
            )
        }
    }

    private suspend fun observeShowSeekControls() {
        settingsRepository.miniPlayerShowSeekControls.collect {
            _uiState.value = _uiState.value.copy(
                showSeekControls = it
            )
        }
    }

    private suspend fun observeControlsLayoutIsDefault() {
        settingsRepository.controlsLayoutIsDefault.collect {
            _uiState.value = _uiState.value.copy(
                controlsLayoutIsDefault = it
            )
        }
    }

    private suspend fun observeShowLyrics() {
        settingsRepository.showLyrics.collect {
            _uiState.value = _uiState.value.copy(
                showLyrics = it
            )
        }
    }

    private suspend fun observeShuffle() {
        settingsRepository.shuffle.collect {
            _uiState.value = _uiState.value.copy(
                shuffle = it
            )
        }
    }

    private suspend fun observeLoopMode() {
        settingsRepository.currentLoopMode.collect {
            _uiState.value = _uiState.value.copy(
                currentLoopMode = it
            )
            musicServiceConnection.setRepeatMode( it.toRepeatMode() )
        }
    }

    private suspend fun observePauseOnCurrentSongEnd() {
        settingsRepository.pauseOnCurrentSongEnd.collect {
            _uiState.value = _uiState.value.copy(
                pauseOnCurrentSongEnd = it
            )
        }
    }

    private suspend fun observeCurrentPlayingSpeed() {
        settingsRepository.currentPlayingSpeed.collect {
            _uiState.value = _uiState.value.copy(
                currentPlayingSpeed = it
            )
            musicServiceConnection.setPlaybackSpeed( it )
        }
    }

    private suspend fun observeCurrentPlayingPitch() {
        settingsRepository.currentPlayingPitch.collect {
            _uiState.value = _uiState.value.copy(
                currentPlayingPitch = it
            )
            musicServiceConnection.setPlaybackPitch( it )
        }
    }

    fun onFavorite( songId: String ) {}

    fun playPause() {
        musicServiceConnection.player?.let {
           if ( it.isPlaying ) it.pause() else it.play()
        }
    }

    fun playPreviousSong(): Boolean {
        musicServiceConnection.player?.let {
            it.seekToPrevious()
            return true
        }
        return false
    }

    fun playNextSong(): Boolean {
        musicServiceConnection.player?.let {
            it.seekToNext()
            return true
        }
        return false
    }

    fun fastRewind() {
        musicServiceConnection.player?.seekBack()
    }

    fun fastForward() {
        musicServiceConnection.player?.seekForward()
    }

    fun onSeekEnd( position: Long ) {
        musicServiceConnection.player?.seekTo( position )
    }

    fun onArtworkClicked() {}

    fun toggleLoopMode() {
        val currentLoopModePosition = LoopMode.entries.indexOf(
            settingsRepository.currentLoopMode.value )
        val nextLoopModePosition = ( currentLoopModePosition + 1 ) % LoopMode.entries.size
        viewModelScope.launch {
            settingsRepository.setCurrentLoopMode( LoopMode.entries[ nextLoopModePosition ] )
        }
    }

    fun toggleShuffleMode() {}

    fun togglePauseOnCurrentSongEnd() {}

    fun onPlayingSpeedChange( playingSpeed: Float ) {
        viewModelScope.launch {
            settingsRepository.setCurrentPlayingSpeed( playingSpeed )
        }
    }

    fun onPlayingPitchChange( playingPitch: Float ) {
        viewModelScope.launch {
            settingsRepository.setCurrentPlayingPitch( playingPitch )
        }
    }

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

data class NowPlayingScreenUiState(
    val currentlyPlayingSong: Song?,
    val playbackPosition: PlaybackPosition,
    val currentlyPlayingSongIndex: Int,
    val queueSize: Int,
    val language: Language,
    val currentlyPlayingSongIsFavorite: Boolean,
    val controlsLayoutIsDefault: Boolean,
    val isPlaying: Boolean,
    val showTrackControls: Boolean,
    val showSeekControls: Boolean,
    val showLyrics: Boolean,
    val shuffle: Boolean,
    val currentLoopMode: LoopMode,
    val pauseOnCurrentSongEnd: Boolean,
    val currentPlayingSpeed: Float,
    val currentPlayingPitch: Float,
    val themeMode: ThemeMode,
    val textMarquee: Boolean,
)


private const val POSITION_UPDATE_INTERVAL_MILLIS = 1L