package com.odesa.musically.ui.nowPlaying

import androidx.media3.common.Player
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.fakes.FakeMusicServiceConnection
import com.odesa.musically.fakes.FakeSettingsRepository
import com.odesa.musically.fakes.id1
import com.odesa.musically.fakes.testMediaItems
import com.odesa.musically.services.media.connection.PlaybackState
import com.odesa.musically.ui.components.PlaybackPosition
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
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
    fun testCurrentlyPlayingSongIsCorrectlyUpdated() {
        assertNull( nowPlayingViewModel.bottomSheetUiState.value.currentlyPlayingSong )
        assertNull( nowPlayingViewModel.bottomBarUiState.value.currentlyPlayingSong )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertNotNull( nowPlayingViewModel.bottomSheetUiState.value.currentlyPlayingSong )
        assertNotNull( nowPlayingViewModel.bottomBarUiState.value.currentlyPlayingSong )
        assertEquals( id1, nowPlayingViewModel.bottomSheetUiState.value.currentlyPlayingSong!!.id )
        assertEquals( id1, nowPlayingViewModel.bottomBarUiState.value.currentlyPlayingSong!!.id )

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
            PlaybackState( Player.STATE_READY, playWhenReady = true, duration = 30000L )
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

    @Test
    fun testUpdatePlaybackPositionIsCorrectlyUpdated() {
        assertFalse( nowPlayingViewModel.updatePlaybackPosition.value )
        musicServiceConnection.setPlaybackState(
            PlaybackState( Player.STATE_READY, playWhenReady = true, duration = 50000L )
        )
        assertTrue( nowPlayingViewModel.updatePlaybackPosition.value )
        musicServiceConnection.setPlaybackState( PlaybackState( Player.STATE_ENDED ) )
        assertFalse( nowPlayingViewModel.updatePlaybackPosition.value )
    }

    @Test
    fun testQueueSizeIsCorrectlyUpdated() {
        assertEquals( 0, nowPlayingViewModel.bottomSheetUiState.value.queueSize )
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( 3, nowPlayingViewModel.bottomSheetUiState.value.queueSize )
    }

    @Test
    fun testCurrentlyPlayingSongIndexIsCorrectlyUpdated() {
        assertEquals( 0,
            nowPlayingViewModel.bottomSheetUiState.value.currentlyPlayingSongIndex )
        musicServiceConnection.setCurrentMediaItemIndex( 10 )
        assertEquals( 10,
            nowPlayingViewModel.bottomSheetUiState.value.currentlyPlayingSongIndex )
    }

}