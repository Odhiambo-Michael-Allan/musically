package com.odesa.musicMatters.ui.playlists

import com.odesa.musicMatters.core.data.playlists.PlaylistRepository
import com.odesa.musicMatters.core.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.core.data.settings.SettingsRepository
import com.odesa.musicMatters.core.datatesting.connection.FakeMusicServiceConnection
import com.odesa.musicMatters.core.datatesting.playlist.FakePlaylistRepository
import com.odesa.musicMatters.core.datatesting.playlists.testPlaylists
import com.odesa.musicMatters.core.datatesting.repository.FakeSettingsRepository
import com.odesa.musicMatters.core.datatesting.songs.testSongs
import com.odesa.musicMatters.core.designsystem.theme.ThemeMode
import com.odesa.musicMatters.core.i8n.Belarusian
import com.odesa.musicMatters.core.i8n.Chinese
import com.odesa.musicMatters.core.i8n.English
import com.odesa.musicMatters.core.i8n.French
import com.odesa.musicMatters.core.i8n.German
import com.odesa.musicMatters.core.i8n.Language
import com.odesa.musicMatters.core.i8n.Spanish
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class PlaylistsViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var viewModel: PlaylistsViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        viewModel = PlaylistsViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testPlaylistsAreCorrectlyLoaded() {
        musicServiceConnection.setIsInitialized()
        assertEquals( testSongs.size, viewModel.uiState.value.songs.size )
        assertEquals( 3, viewModel.uiState.value.playlists.size )
        assertFalse( viewModel.uiState.value.isLoadingSongs )
    }

    @Test
    fun testPlaylistsAreCorrectlyUpdated() = runTest {
        musicServiceConnection.setIsInitialized()
        playlistRepository.savePlaylist( testPlaylists.first() )
        assertEquals( 4, viewModel.uiState.value.playlists.size )
        playlistRepository.deletePlaylist( testPlaylists.first() )
        assertEquals( 3, viewModel.uiState.value.playlists.size )
    }

    @Test
    fun testPlaylistIsCorrectlyUpdated() = runTest {
        musicServiceConnection.setIsInitialized()
        playlistRepository.playlists.value.forEach {
            playlistRepository.addSongIdToPlaylist( testSongs.first().id, it.id )
        }
        viewModel.uiState.value.playlists.forEach {
            assertEquals( 1, it.songIds.size )
        }
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", viewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo( language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = viewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode, viewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, viewModel.uiState.value.themeMode )
        }
    }
}