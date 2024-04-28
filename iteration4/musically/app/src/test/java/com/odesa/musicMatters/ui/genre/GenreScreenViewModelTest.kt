package com.odesa.musicMatters.ui.genre

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
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class GenreScreenViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var genreScreenViewModel: GenreScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        playlistRepository = FakePlaylistRepository()
        genreScreenViewModel = GenreScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository,
            playlistRepository = playlistRepository
        )
    }

    @Test
    fun testLoadSongsWithSpecificGenre() {
        genreScreenViewModel.loadSongsWithGenre( "Hip Hop" )
        musicServiceConnection.runWhenInitialized {
            assertEquals( 2, genreScreenViewModel.uiState.value.songs.size )
        }
        musicServiceConnection.isInitialized = true
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", genreScreenViewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo(language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = genreScreenViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode, genreScreenViewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, genreScreenViewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", genreScreenViewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, genreScreenViewModel.uiState.value.currentlyPlayingSongId )
    }

    @Test
    fun testFavoriteSongsChange() = runTest {
        assertEquals( 0, genreScreenViewModel.uiState.value.favoriteSongIds.size )
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, genreScreenViewModel.uiState.value.favoriteSongIds.size )
    }

    @Test
    fun testSongIsCorrectlyRemovedFromFavoriteList() = runTest {
        testSongs.forEach {
            playlistRepository.addToFavorites( it.id )
        }
        assertEquals( testSongs.size, genreScreenViewModel.uiState.value.favoriteSongIds.size )
        genreScreenViewModel.addToFavorites( testSongs.first().id )
        assertEquals( testSongs.size - 1, genreScreenViewModel.uiState.value.favoriteSongIds.size )
    }

}