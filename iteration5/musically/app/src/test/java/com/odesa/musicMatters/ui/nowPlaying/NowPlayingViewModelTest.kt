package com.odesa.musicMatters.ui.nowPlaying

import androidx.media3.common.Player
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.LoopMode
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.preferences.impl.allowedSpeedPitchValues
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.id1
import com.odesa.musicMatters.fakes.testMediaItems
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.services.media.connection.PlaybackState
import com.odesa.musicMatters.services.media.extensions.toSong
import com.odesa.musicMatters.ui.components.PlaybackPosition
import com.odesa.musicMatters.ui.theme.ThemeMode
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
    private lateinit var playlistRepository: PlaylistRepository

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        nowPlayingViewModel = NowPlayingViewModel(
            application = ApplicationProvider.getApplicationContext(),
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository,
        )
    }

    @Test
    fun testCurrentlyPlayingSongIsCorrectlyUpdated() {
        assertNull( nowPlayingViewModel.uiState.value.currentlyPlayingSong )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertNotNull( nowPlayingViewModel.uiState.value.currentlyPlayingSong )
        assertEquals( id1, nowPlayingViewModel.uiState.value.currentlyPlayingSong!!.id )

    }

    @Test
    fun testPlaybackStateIsCorrectlyUpdated() {
        assertEquals(
            PlaybackPosition.zero.played,
            nowPlayingViewModel.uiState.value.playbackPosition.played
        )
        assertEquals(
            PlaybackPosition.zero.total,
            nowPlayingViewModel.uiState.value.playbackPosition.total
        )
        musicServiceConnection.setPlaybackState(
            PlaybackState( Player.STATE_READY, playWhenReady = true, duration = 30000L )
        )
        assertEquals(
            30000L,
            nowPlayingViewModel.uiState.value.playbackPosition.total
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
        assertEquals( 0, nowPlayingViewModel.uiState.value.queueSize )
        musicServiceConnection.setMediaItems( testMediaItems )
        assertEquals( 3, nowPlayingViewModel.uiState.value.queueSize )
    }

    @Test
    fun testCurrentlyPlayingSongIndexIsCorrectlyUpdated() {
        assertEquals( 0,
            nowPlayingViewModel.uiState.value.currentlyPlayingSongIndex )
        musicServiceConnection.setCurrentMediaItemIndex( 10 )
        assertEquals( 10,
            nowPlayingViewModel.uiState.value.currentlyPlayingSongIndex )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings",
            nowPlayingViewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = nowPlayingViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testIsPlayingChange() {
        assertFalse( nowPlayingViewModel.uiState.value.isPlaying )
        musicServiceConnection.setIsPlaying( true )
        assertTrue( nowPlayingViewModel.uiState.value.isPlaying )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode,
            nowPlayingViewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, nowPlayingViewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testTextMarqueeChange() = runTest {
        assertEquals( SettingsDefaults.miniPlayerTextMarquee,
            nowPlayingViewModel.uiState.value.textMarquee )
        settingsRepository.setMiniPlayerTextMarquee( false )
        assertFalse( nowPlayingViewModel.uiState.value.textMarquee )
        settingsRepository.setMiniPlayerTextMarquee( true )
        assertTrue( nowPlayingViewModel.uiState.value.textMarquee )
    }

    @Test
    fun testShowTrackControlsChange() = runTest {
        assertEquals( SettingsDefaults.miniPlayerShowTrackControls,
            nowPlayingViewModel.uiState.value.showTrackControls )
        settingsRepository.setMiniPlayerShowTrackControls( false )
        assertFalse( nowPlayingViewModel.uiState.value.showTrackControls )
        settingsRepository.setMiniPlayerShowTrackControls( true )
        assertTrue( nowPlayingViewModel.uiState.value.showTrackControls )
    }

    @Test
    fun testShowSeekControlsChange() = runTest {
        assertEquals( SettingsDefaults.miniPlayerShowSeekControls,
            nowPlayingViewModel.uiState.value.showSeekControls )
        settingsRepository.setMiniPlayerShowSeekControls( true )
        assertTrue( nowPlayingViewModel.uiState.value.showSeekControls )
        settingsRepository.setMiniPlayerShowSeekControls( false )
        assertFalse( nowPlayingViewModel.uiState.value.showSeekControls )
    }

    @Test
    fun testControlsLayoutIsDefaultChange() = runTest {
        assertEquals( SettingsDefaults.controlsLayoutIsDefault,
            nowPlayingViewModel.uiState.value.controlsLayoutIsDefault )
        listOf( true, false ).forEach {
            settingsRepository.setControlsLayoutIsDefault( it )
            assertEquals( it,
                nowPlayingViewModel.uiState.value.controlsLayoutIsDefault )
        }
    }

    @Test
    fun testShowLyricsChange() = runTest {
        assertEquals( SettingsDefaults.showLyrics,
            nowPlayingViewModel.uiState.value.showLyrics )
        listOf( true, false ).forEach {
            settingsRepository.setShowLyrics( it )
            assertEquals( it,
                nowPlayingViewModel.uiState.value.showLyrics )
        }
    }

    @Test
    fun testShuffleChange() = runTest {
        assertEquals( SettingsDefaults.shuffle,
            nowPlayingViewModel.uiState.value.shuffle )
        listOf( true, false ).forEach {
            settingsRepository.setShuffle( it )
            assertEquals( it,
                nowPlayingViewModel.uiState.value.shuffle )
        }
    }

    @Test
    fun testLoopModeChange() = runTest {
        assertEquals( SettingsDefaults.loopMode,
            nowPlayingViewModel.uiState.value.currentLoopMode )
        LoopMode.entries.forEach {
            settingsRepository.setCurrentLoopMode( it )
            assertEquals( it, nowPlayingViewModel.uiState.value.currentLoopMode )
        }
    }

    @Test
    fun testPlayingSpeedChange() = runTest {
        assertEquals( SettingsDefaults.currentPlayingSpeed,
            nowPlayingViewModel.uiState.value.currentPlayingSpeed )
        allowedSpeedPitchValues.forEach {
            settingsRepository.setCurrentPlayingSpeed( it )
            assertEquals( it,
                nowPlayingViewModel.uiState.value.currentPlayingSpeed )
        }
    }

    @Test
    fun testPlayingPitchChange() = runTest {
        assertEquals( SettingsDefaults.currentPlayingPitch,
            nowPlayingViewModel.uiState.value.currentPlayingPitch )
        allowedSpeedPitchValues.forEach {
            settingsRepository.setCurrentPlayingPitch( it )
            assertEquals( it,
                nowPlayingViewModel.uiState.value.currentPlayingPitch )
        }
    }

    @Test
    fun testToggleCurrentLoopMode() {
        assertEquals( SettingsDefaults.loopMode, nowPlayingViewModel.uiState.value.currentLoopMode )
        nowPlayingViewModel.toggleLoopMode()
        assertEquals( LoopMode.Song, nowPlayingViewModel.uiState.value.currentLoopMode )
        nowPlayingViewModel.toggleLoopMode()
        assertEquals( LoopMode.Queue, nowPlayingViewModel.uiState.value.currentLoopMode )
        nowPlayingViewModel.toggleLoopMode()
        assertEquals( SettingsDefaults.loopMode, nowPlayingViewModel.uiState.value.currentLoopMode )
    }

    @Test
    fun testToggleShuffleMode() {
        assertEquals( SettingsDefaults.shuffle, nowPlayingViewModel.uiState.value.shuffle )
        nowPlayingViewModel.toggleShuffleMode()
        assertEquals( !SettingsDefaults.shuffle, nowPlayingViewModel.uiState.value.shuffle )
        nowPlayingViewModel.toggleShuffleMode()
        assertEquals( SettingsDefaults.shuffle, nowPlayingViewModel.uiState.value.shuffle )
    }

    @Test
    fun testCurrentlyPlayingSongIsFavoriteChange() = runTest {
        val currentlyPlayingSongId = testMediaItems.first().toSong( setOf() )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        playlistRepository.addToFavorites( currentlyPlayingSongId.id )
        assertTrue( nowPlayingViewModel.uiState.value.currentlyPlayingSongIsFavorite )
        playlistRepository.removeFromFavorites( currentlyPlayingSongId.id )
        assertFalse( nowPlayingViewModel.uiState.value.currentlyPlayingSongIsFavorite )
    }

}