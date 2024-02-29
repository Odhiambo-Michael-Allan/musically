package com.odesa.musically.ui.nowPlaying

import androidx.media3.common.Player
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odesa.musically.data.settings.SettingsRepository
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.fakes.FakeMusicServiceConnection
import com.odesa.musically.fakes.FakeSettingsRepository
import com.odesa.musically.fakes.id1
import com.odesa.musically.fakes.testMediaItems
import com.odesa.musically.services.i18n.Belarusian
import com.odesa.musically.services.i18n.Chinese
import com.odesa.musically.services.i18n.English
import com.odesa.musically.services.i18n.French
import com.odesa.musically.services.i18n.German
import com.odesa.musically.services.i18n.Language
import com.odesa.musically.services.i18n.Spanish
import com.odesa.musically.services.media.connection.PlaybackState
import com.odesa.musically.ui.components.PlaybackPosition
import com.odesa.musically.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
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
        assertNull( nowPlayingViewModel.nowPlayingScreenUiState.value.currentlyPlayingSong )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertNotNull( nowPlayingViewModel.nowPlayingScreenUiState.value.currentlyPlayingSong )
        assertEquals( id1, nowPlayingViewModel.nowPlayingScreenUiState.value.currentlyPlayingSong!!.id )

    }

    @Test
    fun testPlaybackStateIsCorrectlyUpdated() {
        assertEquals(
            PlaybackPosition.zero.played,
            nowPlayingViewModel.nowPlayingScreenUiState.value.playbackPosition.played
        )
        assertEquals(
            PlaybackPosition.zero.total,
            nowPlayingViewModel.nowPlayingScreenUiState.value.playbackPosition.total
        )
        musicServiceConnection.setPlaybackState(
            PlaybackState( Player.STATE_READY, playWhenReady = true, duration = 30000L )
        )
        assertEquals(
            30000L,
            nowPlayingViewModel.nowPlayingScreenUiState.value.playbackPosition.total
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
        assertEquals( 0, nowPlayingViewModel.nowPlayingScreenUiState.value.queueSize )
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( 3, nowPlayingViewModel.nowPlayingScreenUiState.value.queueSize )
    }

    @Test
    fun testCurrentlyPlayingSongIndexIsCorrectlyUpdated() {
        assertEquals( 0,
            nowPlayingViewModel.nowPlayingScreenUiState.value.currentlyPlayingSongIndex )
        musicServiceConnection.setCurrentMediaItemIndex( 10 )
        assertEquals( 10,
            nowPlayingViewModel.nowPlayingScreenUiState.value.currentlyPlayingSongIndex )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings",
            nowPlayingViewModel.nowPlayingScreenUiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = nowPlayingViewModel.nowPlayingScreenUiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testIsPlayingChange() {
        assertFalse( nowPlayingViewModel.nowPlayingScreenUiState.value.isPlaying )
        musicServiceConnection.setIsPlaying( true )
        assertTrue( nowPlayingViewModel.nowPlayingScreenUiState.value.isPlaying )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode,
            nowPlayingViewModel.nowPlayingScreenUiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, nowPlayingViewModel.nowPlayingScreenUiState.value.themeMode )
        }
    }

    @Test
    fun testTextMarqueeChange() = runTest {
        assertEquals( SettingsDefaults.miniPlayerTextMarquee,
            nowPlayingViewModel.nowPlayingScreenUiState.value.textMarquee )
        settingsRepository.setMiniPlayerTextMarquee( false )
        assertFalse( nowPlayingViewModel.nowPlayingScreenUiState.value.textMarquee )
        settingsRepository.setMiniPlayerTextMarquee( true )
        assertTrue( nowPlayingViewModel.nowPlayingScreenUiState.value.textMarquee )
    }

    @Test
    fun testShowTrackControlsChange() = runTest {
        assertEquals( SettingsDefaults.miniPlayerShowTrackControls,
            nowPlayingViewModel.nowPlayingScreenUiState.value.showTrackControls )
        settingsRepository.setMiniPlayerShowTrackControls( false )
        assertFalse( nowPlayingViewModel.nowPlayingScreenUiState.value.showTrackControls )
        settingsRepository.setMiniPlayerShowTrackControls( true )
        assertTrue( nowPlayingViewModel.nowPlayingScreenUiState.value.showTrackControls )
    }

    @Test
    fun testShowSeekControlsChange() = runTest {
        assertEquals( SettingsDefaults.miniPlayerShowSeekControls,
            nowPlayingViewModel.nowPlayingScreenUiState.value.showSeekControls )
        settingsRepository.setMiniPlayerShowSeekControls( true )
        assertTrue( nowPlayingViewModel.nowPlayingScreenUiState.value.showSeekControls )
        settingsRepository.setMiniPlayerShowSeekControls( false )
        assertFalse( nowPlayingViewModel.nowPlayingScreenUiState.value.showSeekControls )
    }

}