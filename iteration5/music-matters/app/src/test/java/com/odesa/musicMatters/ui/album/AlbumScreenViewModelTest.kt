package com.odesa.musicMatters.ui.album

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.playlists.testPlaylists
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
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
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
    private lateinit var viewModel: AlbumScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        viewModel = AlbumScreenViewModel(
            albumName = "Album-4",
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLanguageChange() {
        assertEquals("Settings", viewModel.uiState.value.language.settings)
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = viewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals(
            SettingsDefaults.themeMode,
            viewModel.uiState.value.themeMode
        )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, viewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", viewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, viewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals( 0, viewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals(
            testSongs.size,
            viewModel.uiState.value.favoriteSongIds.size
        )
    }

    @Test
    fun testSongIsCorrectlyRemovedFromFavoriteList() = runTest {
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals(
            testSongs.size,
            viewModel.uiState.value.favoriteSongIds.size
        )
        viewModel.addToFavorites( testSongs.first().id )
        assertEquals(
            testSongs.size - 1,
            viewModel.uiState.value.favoriteSongIds.size
        )
    }

    @Test
    fun testLoadSongsInAlbum() = runTest {
        assertEquals( 0, viewModel.uiState.value.songsInAlbum.size )
        assertTrue( viewModel.uiState.value.isLoadingSongsInAlbum )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isLoadingSongsInAlbum )
        assertEquals( 2, viewModel.uiState.value.songsInAlbum.size )
        assertNotNull( viewModel.uiState.value.album )
        assertEquals( "Album-4", viewModel.uiState.value.album?.name )
        assertFalse( viewModel.uiState.value.isLoadingSongsInAlbum )
    }

    @Test
    fun testPlaylistsAreCorrectlyUpdated() = runTest {
        assertEquals( 1, viewModel.uiState.value.playlists.size )
        testPlaylists.forEach {
            playlistRepository.savePlaylist( it )
        }
        assertEquals( testPlaylists.size + 1, viewModel.uiState.value.playlists.size )
    }
}