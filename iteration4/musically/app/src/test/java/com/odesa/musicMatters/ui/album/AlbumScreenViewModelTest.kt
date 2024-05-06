package com.odesa.musicMatters.ui.album

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
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
import com.odesa.musicMatters.services.media.testSongs
import com.odesa.musicMatters.ui.theme.ThemeMode
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class AlbumScreenViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var albumScreenViewModel: AlbumScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        albumScreenViewModel = AlbumScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLanguageChange() {
        TestCase.assertEquals("Settings", albumScreenViewModel.uiState.value.language.settings)
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = albumScreenViewModel.uiState.value.language
        TestCase.assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testThemeModeChange() = runTest {
        TestCase.assertEquals(
            SettingsDefaults.themeMode,
            albumScreenViewModel.uiState.value.themeMode
        )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            TestCase.assertEquals( it, albumScreenViewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        TestCase.assertEquals( "", albumScreenViewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        TestCase.assertEquals( id1, albumScreenViewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        TestCase.assertEquals( 0, albumScreenViewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        TestCase.assertEquals(
            testSongs.size,
            albumScreenViewModel.uiState.value.favoriteSongIds.size
        )
    }

    @Test
    fun testSongIsCorrectlyRemovedFromFavoriteList() = runTest {
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        TestCase.assertEquals(
            testSongs.size,
            albumScreenViewModel.uiState.value.favoriteSongIds.size
        )
        albumScreenViewModel.addToFavorites( testSongs.first().id )
        TestCase.assertEquals(
            testSongs.size - 1,
            albumScreenViewModel.uiState.value.favoriteSongIds.size
        )
    }

    @Test
    fun testLoadSongsInAlbum() = runTest {
        assertEquals( 0, albumScreenViewModel.uiState.value.songsInAlbum.size )
        albumScreenViewModel.loadSongsInAlbum( "Album-4" )
        musicServiceConnection.runWhenInitialized {
            assertEquals( 2, albumScreenViewModel.uiState.value.songsInAlbum.size )
            assertNotNull( albumScreenViewModel.uiState.value.album )
            assertEquals( "Album-4", albumScreenViewModel.uiState.value.album?.name )
            assertFalse( albumScreenViewModel.uiState.value.isLoadingSongsInAlbum )
        }
        musicServiceConnection.isInitialized = true
    }
}