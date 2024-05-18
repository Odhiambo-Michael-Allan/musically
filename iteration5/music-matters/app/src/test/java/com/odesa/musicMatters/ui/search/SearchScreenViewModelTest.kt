package com.odesa.musicMatters.ui.search

import com.odesa.musicMatters.data.playlists.PlaylistRepository
import com.odesa.musicMatters.data.preferences.impl.SettingsDefaults
import com.odesa.musicMatters.data.search.SearchHistoryRepository
import com.odesa.musicMatters.data.search.impl.testSearchHistoryItems
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakePlaylistRepository
import com.odesa.musicMatters.fakes.FakeSearchHistoryRepository
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
class SearchScreenViewModelTest {

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var searchHistoryRepository: SearchHistoryRepository
    private lateinit var viewModel: SearchScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        playlistRepository = FakePlaylistRepository()
        settingsRepository = FakeSettingsRepository()
        searchHistoryRepository = FakeSearchHistoryRepository()
        viewModel = SearchScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            playlistRepository = playlistRepository,
            settingsRepository = settingsRepository,
            searchHistoryRepository = searchHistoryRepository
        )
    }

    @Test
    fun testSearchHistoryItemsAreSavedCorrectly() = runTest {
        assertEquals( 0, viewModel.uiState.value.searchHistoryItems.size )
        testSearchHistoryItems.forEach {
            viewModel.saveSearchHistoryItem( it )
        }
        assertEquals( testSearchHistoryItems.size, viewModel.uiState.value.searchHistoryItems.size )
    }

    @Test
    fun testSearchHistoryItemsAreDeletedCorrectly() = runTest {
        testSearchHistoryItems.forEach {
            viewModel.saveSearchHistoryItem( it )
        }
        viewModel.deleteSearchHistoryItem( testSearchHistoryItems.first() )
        viewModel.deleteSearchHistoryItem( testSearchHistoryItems.last() )
        assertEquals( testSearchHistoryItems.size - 2, viewModel.uiState.value.searchHistoryItems.size )
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
    fun testMusicServiceConnectionInitializationStatusIsCorrectlyUpdated() {
        assertTrue( viewModel.uiState.value.isLoadingSearchHistory )
        musicServiceConnection.setIsInitialized()
        assertFalse( viewModel.uiState.value.isLoadingSearchHistory )
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", viewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, viewModel.uiState.value.currentlyPlayingSongId )
    }
}