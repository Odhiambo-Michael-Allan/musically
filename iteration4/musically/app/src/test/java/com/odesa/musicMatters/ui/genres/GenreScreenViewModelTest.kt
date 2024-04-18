package com.odesa.musicMatters.ui.genres

import com.odesa.musicMatters.MainCoroutineRule
import com.odesa.musicMatters.data.settings.SettingsRepository
import com.odesa.musicMatters.fakes.FakeMusicServiceConnection
import com.odesa.musicMatters.fakes.FakeSettingsRepository
import com.odesa.musicMatters.services.i18n.Belarusian
import com.odesa.musicMatters.services.i18n.Chinese
import com.odesa.musicMatters.services.i18n.English
import com.odesa.musicMatters.services.i18n.French
import com.odesa.musicMatters.services.i18n.German
import com.odesa.musicMatters.services.i18n.Language
import com.odesa.musicMatters.services.i18n.Spanish
import com.odesa.musicMatters.services.media.library.musicList
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith( RobolectricTestRunner::class )
class GenreScreenViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var musicServiceConnection: FakeMusicServiceConnection
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var genreScreenViewModel: GenreScreenViewModel

    @Before
    fun setup() {
        musicServiceConnection = FakeMusicServiceConnection()
        settingsRepository = FakeSettingsRepository()
        genreScreenViewModel = GenreScreenViewModel(
            musicServiceConnection = musicServiceConnection,
            settingsRepository = settingsRepository
        )
        musicServiceConnection.setMediaItems( musicList )
    }

    @Test
    fun testGenresAreCorrectlyLoadedFromMusicServiceConnection() {
        val uiState = genreScreenViewModel.uiState.value
        assertEquals( 3, uiState.genres.size )
    }

    @Test
    fun testNumberOfTracksInGenreAreCorrectlySet() {
        val genreList = genreScreenViewModel.uiState.value.genres
        genreList.forEach { genre ->
            if ( genre.name == "Hip Hop" )
                assertEquals( 2, genre.numberOfTracks )
        }
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
}