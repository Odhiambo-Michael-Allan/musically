package com.odesa.musically.ui.songs

import com.odesa.musically.MainCoroutineRule
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
import com.odesa.musically.ui.theme.ThemeMode
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class SongsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var songsViewModel: SongsViewModel

    @Before
    fun setup() {
        settingsRepository = FakeSettingsRepository()
        musicServiceConnection = FakeMusicServiceConnection()
        songsViewModel = SongsViewModel(
            settingsRepository = settingsRepository,
            musicServiceConnection = musicServiceConnection
        )
    }

    @Test
    fun testLanguageChange() {
        assertEquals( "Settings", songsViewModel.uiState.value.language.settings )
        changeLanguageTo( Belarusian, "Налады" )
        changeLanguageTo( Chinese, "设置" )
        changeLanguageTo( English, "Settings" )
        changeLanguageTo( French, "Paramètres" )
        changeLanguageTo( German, "Einstellungen" )
        changeLanguageTo( Spanish, "Configuración" )
    }

    private fun changeLanguageTo( language: Language, testString: String ) = runTest {
        settingsRepository.setLanguage( language.locale )
        val currentLanguage = songsViewModel.uiState.value.language
        assertEquals( testString, currentLanguage.settings )
    }

    @Test
    fun testThemeModeChange() = runTest {
        assertEquals( SettingsDefaults.themeMode, songsViewModel.uiState.value.themeMode )
        ThemeMode.entries.forEach {
            settingsRepository.setThemeMode( it )
            assertEquals( it, songsViewModel.uiState.value.themeMode )
        }
    }

    @Test
    fun testMediaItemsAreCorrectlyLoadedFromTheMusicServiceConnection() {
        val uiState = songsViewModel.uiState.value
        assertEquals( 3, uiState.songs.size )
    }

    @Test
    fun testNowPlayingMediaItemIsCorrectlyUpdated() {
        assertEquals( "", songsViewModel.uiState.value.currentlyPlayingSongId )
        musicServiceConnection.setNowPlaying( testMediaItems.first() )
        assertEquals( id1, songsViewModel.uiState.value.currentlyPlayingSongId )
    }
}