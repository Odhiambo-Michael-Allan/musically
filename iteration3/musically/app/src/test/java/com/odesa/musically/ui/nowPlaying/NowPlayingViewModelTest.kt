package com.odesa.musically.ui.nowPlaying

import androidx.media3.common.Player
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.fakes.FakeMusicServiceConnection
import com.odesa.musically.fakes.FakeSettingsRepository
import com.odesa.musically.services.media.connection.PlaybackState
import com.odesa.musically.ui.components.PlaybackPosition
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith( AndroidJUnit4::class )
class NowPlayingViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var nowPlayingViewModel: NowPlayingViewModel
    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        nowPlayingViewModel = NowPlayingViewModel(
            application = ApplicationProvider.getApplicationContext(),
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository
        )
    }

    @Test
    fun testPlaybackStateIsCorrectlyUpdated() {
        assertEquals(
            PlaybackPosition.zero.played,
            nowPlayingViewModel.bottomSheetUiState.value.playbackPosition.played
        )
        assertEquals(
            PlaybackPosition.zero.played,
            nowPlayingViewModel.bottomBarUiState.value.playbackPosition.played
        )
        assertEquals(
            PlaybackPosition.zero.total,
            nowPlayingViewModel.bottomSheetUiState.value.playbackPosition.total
        )
        assertEquals(
            PlaybackPosition.zero.total,
            nowPlayingViewModel.bottomBarUiState.value.playbackPosition.total
        )
        musicServiceConnection.setPlaybackState(
            PlaybackState( Player.STATE_IDLE, duration = 30000L )
        )
        assertEquals(
            30000L,
            nowPlayingViewModel.bottomSheetUiState.value.playbackPosition.total
        )
        assertEquals(
            30000L,
            nowPlayingViewModel.bottomBarUiState.value.playbackPosition.total
        )
    }

}