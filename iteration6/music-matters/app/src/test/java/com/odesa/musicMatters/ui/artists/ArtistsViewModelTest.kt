package com.odesa.musicMatters.ui.artists

import android.net.Uri
import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.SortArtistsBy
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.fakes.artistList
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.services.media.Artist
import com.odesa.musicMatters.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class ArtistsViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var viewModel: ArtistsScreenViewModel


    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        viewModel = ArtistsScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testArtistsAreCorrectlyLoadedFromMusicServiceConnection() {
        assertTrue( viewModel.uiState.value.isLoadingArtists )
        musicServiceConnection.setIsInitialized()
        assertEquals( artistList.size, viewModel.uiState.value.artists.size )
        assertFalse( viewModel.uiState.value.isLoadingArtists )
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

    @Test
    fun testSortArtistsByValueIsCorrectlySet() = runTest {
        assertEquals( SettingsDefaults.sortArtistsBy, viewModel.uiState.value.sortArtistsBy )
        SortArtistsBy.entries.forEach {
            viewModel.setSortArtistsBy( it )
            assertEquals( it, settingsRepository.sortArtistsBy.value )
            assertEquals( it, viewModel.uiState.value.sortArtistsBy )
        }
    }

    @Test
    fun testSortArtistsInReverseIsCorrectlySet() = runTest {
        assertFalse( viewModel.uiState.value.sortArtistsInReverse )
        viewModel.setSortArtistsInReverse( true )
        assertTrue( settingsRepository.sortArtistsInReverse.value )
        assertTrue( viewModel.uiState.value.sortArtistsInReverse )
        viewModel.setSortArtistsInReverse( false )
        assertFalse( settingsRepository.sortArtistsInReverse.value )
        assertFalse( viewModel.uiState.value.sortArtistsInReverse )
    }

    @Test
    fun testArtistsAreCorrectlySorted() = runTest {
        musicServiceConnection.setIsInitialized()
        musicServiceConnection.setArtists( testArtistsForSorting )
        assertEquals( "Artist-4", viewModel.uiState.value.artists.first().name )
        viewModel.setSortArtistsInReverse( true )
        assertEquals( "Artist-8", viewModel.uiState.value.artists.first().name )
        viewModel.setSortArtistsInReverse( false )
        viewModel.setSortArtistsBy( SortArtistsBy.TRACKS_COUNT )
        assertEquals( "Artist-6", viewModel.uiState.value.artists.first().name )
        viewModel.setSortArtistsInReverse( true )
        assertEquals( "Artist-8", viewModel.uiState.value.artists.first().name )
        viewModel.setSortArtistsInReverse( false )
        viewModel.setSortArtistsBy( SortArtistsBy.ALBUMS_COUNT )
        assertEquals( "Artist-6", viewModel.uiState.value.artists.first().name )
        viewModel.setSortArtistsInReverse( true )
        assertEquals( "Artist-8", viewModel.uiState.value.artists.first().name )
    }
}

private val testArtistsForSorting = listOf(
    Artist(
        name = "Artist-5",
        artworkUri = Uri.EMPTY,
        albumCount = 5,
        trackCount = 32
    ),
    Artist(
        name = "Artist-6",
        artworkUri = Uri.EMPTY,
        albumCount = 2,
        trackCount = 23
    ),
    Artist(
        name = "Artist-8",
        artworkUri = Uri.EMPTY,
        albumCount = 84,
        trackCount = 343
    ),
    Artist(
        name = "Artist-4",
        artworkUri = Uri.EMPTY,
        albumCount = 8,
        trackCount = 53
    )
)