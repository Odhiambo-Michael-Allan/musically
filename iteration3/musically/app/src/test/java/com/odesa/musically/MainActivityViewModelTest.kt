package com.odesa.musically

import androidx.media3.common.Player
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odesa.musically.fakes.FakeMusicServiceConnection
import com.odesa.musically.services.media.connection.EMPTY_PLAYBACK_STATE
import com.odesa.musically.services.media.connection.PlaybackState
import com.odesa.musically.ui.nowPlaying.NowPlayingViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith( AndroidJUnit4::class )
class MainActivityViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var mainActivityViewModel: NowPlayingViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        mainActivityViewModel = NowPlayingViewModel(
            application = ApplicationProvider.getApplicationContext(),
            musicServiceConnection = musicServiceConnection
        )
    }

    @Test
    fun testNowPlayingMediaDurationIsCorrectlyUpdated() {
        assertEquals(
            EMPTY_PLAYBACK_STATE.duration,
            mainActivityViewModel.nowPlayingUiState.value.currentMediaDuration
        )
        musicServiceConnection.setPlaybackState(
            PlaybackState( Player.STATE_IDLE, duration = 30000L )
        )
        assertEquals(
            30000L,
            mainActivityViewModel.nowPlayingUiState.value.currentMediaDuration
        )
    }

    @Test
    fun testNowPlayingMediaPositionIsCorrectlyUpdated() {
        assertEquals(
            0L,
            mainActivityViewModel.nowPlayingUiState.value.currentMediaPosition
        )
    }
}