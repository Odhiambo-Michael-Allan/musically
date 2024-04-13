package com.odesa.musicMatters.ui.songs

import com.odesa.musicMatters.MainCoroutineRule
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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class SongsScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var songsScreenViewModel: SongsScreenViewModel
    private lateinit var playlistRepository: PlaylistRepository

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        musicServiceConnection = FakeMusicServiceConnection()
        playlistRepository = FakePlaylistRepository()
        songsScreenViewModel = SongsScreenViewModel(
            settingsRepository = settingsRepository,
            musicServiceConnection = musicServiceConnection,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", songsScreenViewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo( language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = songsScreenViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode, songsScreenViewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, songsScreenViewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testMediaItemsAreCorrectlyLoadedFromTheMusicServiceConnection() {
        val uiState = songsScreenViewModel.uiState.value
        assertEquals( 3, uiState.songs.size )
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", songsScreenViewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, songsScreenViewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals( 0, songsScreenViewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, songsScreenViewModel.uiState.value.favoriteSongIds.size )
    }
}